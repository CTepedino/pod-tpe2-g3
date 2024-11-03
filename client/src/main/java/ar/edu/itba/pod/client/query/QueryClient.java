package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.CSVPrintable;
import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import ar.edu.itba.pod.client.util.CSVPrinter;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import ar.edu.itba.pod.client.util.QueryTimer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.query.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public abstract class QueryClient<ValueIn>{
    static final Logger logger = LoggerFactory.getLogger(QueryClient.class);

    static final String GROUP_NAME = "g3";
    private static final String GROUP_PASS = GROUP_NAME + "-pass";

    private static final String TICKET_MAP = GROUP_NAME + "-tickets";

    private final QueryTimer queryTimer;

    final HazelcastInstance hazelcastInstance;
    final QueryPropertiesFactory.QueryProperties properties;
    final CityCSVParserFactory csvParserFactory;

    QueryClient(QueryPropertiesFactory.QueryProperties properties, String timeFileName){
        this.properties = properties;
        hazelcastInstance = startHazelcast(properties.getAddresses());
        csvParserFactory = properties.getCity().getParser(properties.getInPath());
        try {
            queryTimer = new QueryTimer(properties.getOutPath() + timeFileName);
        } catch(IOException e){
            logger.error("Error opening time output file {}", properties.getOutPath());
            throw new IllegalStateException();
        }
    }

    private HazelcastInstance startHazelcast(List<String> addresses){
            GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);

            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
            clientNetworkConfig.setAddresses(addresses);

            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

            return HazelcastClient.newHazelcastClient(clientConfig);
    }

    Set<String> getAgencySet(){
        Set<String> agencies = new HashSet<>();
        csvParserFactory.getAgencyFileParser().consumeAll(agencies::add);
        return agencies;
    }

    Map<String, String> getInfractionsMap(){
        Map<String, String> infractions = new HashMap<>();
        csvParserFactory.getInfractionFileParser().consumeAll(infraction ->
                infractions.put(infraction.getId(), infraction.getDefinition())
        );
        return infractions;
    }

    KeyValueSource<Long, ValueIn> loadTicketData(Function<Ticket, ValueIn> dtoMapper){
        AtomicLong incrementalKey = new AtomicLong();
        IMap<Long, ValueIn> tickets = hazelcastInstance.getMap(TICKET_MAP);
        tickets.clear();
        csvParserFactory.getTicketFileParser().consumeAllParallel( t ->
                tickets.put(incrementalKey.incrementAndGet(), dtoMapper.apply(t))
        );
        return KeyValueSource.fromMap(tickets);
    }

    abstract KeyValueSource<Long, ValueIn> loadData();
    abstract void mapReduceJob(KeyValueSource<Long, ValueIn> keyValueSource) throws ExecutionException, InterruptedException;

    void printResults(String[] headers, String fileName, Iterable<? extends CSVPrintable> results){
        CSVPrinter printer = new CSVPrinter(headers);
        printer.print(properties.getOutPath() + fileName, results);
    }

    public void executeQuery(){

        try {
            queryTimer.startLoad();
            KeyValueSource<Long, ValueIn> keyValueSource = loadData();
            queryTimer.endLoad();

            queryTimer.startJob();
            mapReduceJob(keyValueSource);
            queryTimer.endJob();

            queryTimer.endQuery();

        } catch (ExecutionException | InterruptedException e){
            logger.error("Hazelcast error: {}", e.getMessage());
        } catch (IOException e){
            logger.error("Error writing time file: {}", e.getMessage());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}

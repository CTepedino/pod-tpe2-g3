package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.CSVPrintable;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import ar.edu.itba.pod.client.util.CSVPrinter;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import ar.edu.itba.pod.client.util.QueryTimer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public abstract class QueryClient<KeyIn, ValueIn>{
    static final Logger logger = LoggerFactory.getLogger(QueryClient.class);

    static final String GROUP_NAME = "g3";
    private static final String GROUP_PASS = GROUP_NAME + "-pass";

    protected static final String AGENCY_LIST = GROUP_NAME + "-agencies";
    protected static final String INFRACTION_MAP = GROUP_NAME + "-infractions";
    protected static final String TICKET_MAP = GROUP_NAME + "-tickets";

    private final QueryTimer queryTimer;

    final HazelcastInstance hazelcastInstance;
    final QueryPropertiesFactory.QueryProperties properties;
    final CityCSVParserFactory csvParserFactory;

    QueryClient(QueryPropertiesFactory.QueryProperties properties, String timeFileName){
        this.properties = properties;
        hazelcastInstance = startHazelcast(properties.getAddresses());
        csvParserFactory = properties.getCity().getParser(properties.getInPath());
        queryTimer = new QueryTimer(properties.getOutPath() + timeFileName);
    }

    private HazelcastInstance startHazelcast(List<String> addresses){
            GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);

            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
            clientNetworkConfig.setAddresses(addresses);

            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

            return HazelcastClient.newHazelcastClient(clientConfig);
    }

    void fillAgencyList(){
        IList<String> agencies = hazelcastInstance.getList(AGENCY_LIST);
        agencies.clear();
        csvParserFactory.getAgencyFileParser().consumeAll(agencies::add);
    }

    void fillInfractionsMap(){
        IMap<String, String> infractions = hazelcastInstance.getMap(INFRACTION_MAP);
        infractions.clear();
        csvParserFactory.getInfractionFileParser().consumeAll(i ->
            infractions.put(i.getId(), i.getDefinition())
        );
    }

    abstract KeyValueSource<KeyIn, ValueIn> loadData();
    abstract void mapReduceJob(KeyValueSource<KeyIn, ValueIn> keyValueSource) throws ExecutionException, InterruptedException;

    void printResults(String[] headers, String fileName, Iterable<? extends CSVPrintable> results){
        CSVPrinter printer = new CSVPrinter(headers);
        printer.print(properties.getOutPath() + fileName, results);
    }

    public void executeQuery(){

        try {

            queryTimer.startLoad();
            KeyValueSource<KeyIn, ValueIn> keyValueSource = loadData();
            queryTimer.endLoad();

            queryTimer.startJob();
            mapReduceJob(keyValueSource);
            queryTimer.endJob();

        } catch (ExecutionException | InterruptedException e){
            logger.error("Hazelcast error: {}", e.getMessage());
        } catch (IOException e){
            logger.error("Error writing time file: {}", e.getMessage());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}

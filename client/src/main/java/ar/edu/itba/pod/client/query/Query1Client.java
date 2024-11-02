package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import query1.TotalTicketsByInfractionAndAgencyCollator;
import query1.TotalTicketsByInfractionAndAgencyCombinerFactory;
import query1.TotalTicketsByInfractionAndAgencyMapper;
import query1.TotalTicketsByInfractionAndAgencyReducerFactory;

import java.io.IOException;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query1Client{

    private static final Logger logger = LoggerFactory.getLogger(Query1Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        logger.info("Query1 Client starting...");

        QueryPropertiesParserFactory.QueryPropertiesParser properties = new QueryPropertiesParserFactory().build();

        try {
            HazelcastInstance hazelcastInstance = ClientUtils.startHazelcast(properties.getAddresses());

            CityCSVParserFactory parserFactory = properties.getCity().getParser(properties.getInPath());

            IMap<String, String> infractions = hazelcastInstance.getMap("g3-infractions");
            infractions.clear();
            parserFactory.getInfractionFileParser().consumeAll( i ->
                    infractions.put(i.getId(), i.getDefinition())
            );

            IList<String> agencies = hazelcastInstance.getList("g3-agencies");
            agencies.clear();
            parserFactory.getAgencyFileParser().consumeAll(agencies::add);


            AtomicLong incrementalKey = new AtomicLong();
            IMap<Long, InfractionAgencyPair> tickets = hazelcastInstance.getMap("g3-tickets");
            tickets.clear();
            parserFactory.getTicketFileParser().consumeAll( t ->
                    tickets.put(incrementalKey.incrementAndGet(), new InfractionAgencyPair(t.getIssuingAgency(), t.getInfractionId()))
            );

            KeyValueSource<Long, InfractionAgencyPair> ticketsKeyValueSource = KeyValueSource.fromMap(tickets);

            JobTracker jobTracker = hazelcastInstance.getJobTracker("g3-ticket-count");
            Job<Long, InfractionAgencyPair> job = jobTracker.newJob(ticketsKeyValueSource);
            ICompletableFuture<SortedSet<InfractionAgencyTicketCount>> future = job
                    .mapper(new TotalTicketsByInfractionAndAgencyMapper())
                    .combiner(new TotalTicketsByInfractionAndAgencyCombinerFactory())
                    .reducer(new TotalTicketsByInfractionAndAgencyReducerFactory())
                    .submit(new TotalTicketsByInfractionAndAgencyCollator(infractions));

            SortedSet<InfractionAgencyTicketCount> set = future.get();

            CSVPrinter printer = new CSVPrinter(new String[]{"Infraction", "Agency", "Tickets"}, ';');
            printer.print(properties.getOutPath() + "/query1.csv", set);


        } finally {
            HazelcastClient.shutdownAll();
        }

    }

}

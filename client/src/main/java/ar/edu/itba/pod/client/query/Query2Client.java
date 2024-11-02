package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.Job;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query2Client {

    private static final Logger logger = LoggerFactory.getLogger(Query2Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        logger.info("Query2 Client starting...");

        QueryPropertiesFactory.QueryProperties properties = new QueryPropertiesFactory().build();

        try {
            //TODO: cambialo para extienda QueryClient, asi no hay tanto codigo repetido
            HazelcastInstance hazelcastInstance = null;//ClientUtils.startHazelcast(properties.getAddresses());

            CityCSVParserFactory parserFactory = properties.getCity().getParser(properties.getInPath());

            IList<String> agencies = hazelcastInstance.getList("g3-agencies");
            agencies.clear();
            parserFactory.getAgencyFileParser().consumeAll(agencies::add);

            AtomicLong incrementalKey = new AtomicLong();
            IMap<Long, Ticket> tickets = hazelcastInstance.getMap("g3-tickets");
            tickets.clear();
            parserFactory.getTicketFileParser().consumeAll( t ->
                    tickets.putIfAbsent(incrementalKey.incrementAndGet(), t)
            );

            KeyValueSource<Long, Ticket> ticketsKeyValueSource = KeyValueSource.fromMap(tickets);

            JobTracker jobTracker = hazelcastInstance.getJobTracker("g3-agency-ytd");
            Job<Long, Ticket> job = jobTracker.newJob(ticketsKeyValueSource);



        } finally {
            HazelcastClient.shutdownAll();
        }

    }

}

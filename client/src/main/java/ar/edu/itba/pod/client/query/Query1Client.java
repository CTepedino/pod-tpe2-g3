package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
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

            AtomicLong incrementalKey = new AtomicLong();
            Map<Long, Ticket> tickets = hazelcastInstance.getMap("g3-ticket");
            tickets.clear();
            parserFactory.getTicketFileParser().consumeAll( t ->
                    tickets.put(incrementalKey.incrementAndGet(), t)
            );


        } finally {
            HazelcastClient.shutdownAll();
        }

    }

}

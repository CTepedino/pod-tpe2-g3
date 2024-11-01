package ar.edu.itba.pod.client.query;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Query1Client{

    private static final Logger logger = LoggerFactory.getLogger(Query1Client.class);

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        logger.info("Query1 Client starting...");

        QueryPropertiesParserFactory.QueryPropertiesParser parser = new QueryPropertiesParserFactory().build();


        try {
            HazelcastInstance hazelcastInstance = ClientUtils.startHazelcast(parser.getAddresses());



        } finally {
            HazelcastClient.shutdownAll();
        }



    }

}

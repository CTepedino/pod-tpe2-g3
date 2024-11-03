package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyFine;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import ar.edu.itba.pod.api.model.dto.InfractionRange;
import ar.edu.itba.pod.api.query4.TopNInfractionsByFineRangeCollator;
import ar.edu.itba.pod.api.query4.TopNInfractionsByFineRangeCombinerFactory;
import ar.edu.itba.pod.api.query4.TopNInfractionsByFineRangeMapper;
import ar.edu.itba.pod.api.query4.TopNInfractionsByFineRangeReducerFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query4Client extends QueryClient<Long, InfractionAgencyFine>{
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-top-infractions";
    private static final String[] OUT_CSV_HEADERS = {"Infraction", "Min", "Max", "Diff"};
    private static final String OUT_CSV_FILENAME = "/query4.csv";
    private static final String OUT_TIME_FILENAME = "/time4.txt";
    private static final int MINIMUM_N = 1;

    private Map<String, String> infractions;

    public Query4Client(){
        super(new QueryPropertiesFactory()
                .useN(MINIMUM_N)
                .useAgency()
                .build(),
            OUT_TIME_FILENAME);
    }

    @Override
    KeyValueSource<Long, InfractionAgencyFine> loadData() {
        infractions = getInfractionsMap();
        AtomicLong incrementalKey = new AtomicLong();
        IMap<Long, InfractionAgencyFine> tickets = hazelcastInstance.getMap(TICKET_MAP);
        tickets.clear();
        csvParserFactory.getTicketFileParser().consumeAll( t ->
            tickets.put(incrementalKey.incrementAndGet(), new InfractionAgencyFine(t.getInfractionId(), t.getIssuingAgency(), t.getFineAmount()))
        );
        return KeyValueSource.fromMap(tickets);
    }

    @Override
    void mapReduceJob(KeyValueSource<Long, InfractionAgencyFine> keyValueSource) throws ExecutionException, InterruptedException {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, InfractionAgencyFine> job = jobTracker.newJob(keyValueSource);

        ICompletableFuture<List<InfractionRange>> future = job
                .mapper(new TopNInfractionsByFineRangeMapper(properties.getAgency(), infractions))
                .combiner(new TopNInfractionsByFineRangeCombinerFactory())
                .reducer(new TopNInfractionsByFineRangeReducerFactory())
                .submit(new TopNInfractionsByFineRangeCollator(infractions, properties.getN()));

        List<InfractionRange> list = future.get();
        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, list);
    }

    public static void main(String[] args){
        new Query4Client().executeQuery();
    }
}

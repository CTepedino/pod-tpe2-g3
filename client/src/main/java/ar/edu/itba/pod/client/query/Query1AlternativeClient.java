package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import query1.*;

import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query1AlternativeClient extends QueryClient<Long, InfractionAgencyPair> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-ticket-count-a";
    private static final String[] OUT_CSV_HEADERS = {"Infraction", "Agency", "Tickets"};
    private static final String OUT_CSV_FILENAME = "/query1a.csv";
    private static final String OUT_TIME_FILENAME = "/time1a.txt";

    public Query1AlternativeClient() {
        super(new QueryPropertiesFactory().build(), OUT_TIME_FILENAME);
    }

    @Override
    public KeyValueSource<Long, InfractionAgencyPair> loadData(){
        fillAgencyList();
        fillInfractionsMap();
        AtomicLong incrementalKey = new AtomicLong();
        IMap<Long, InfractionAgencyPair> tickets = hazelcastInstance.getMap(TICKET_MAP);
        tickets.clear();
        csvParserFactory.getTicketFileParser().consumeAll( t ->
                tickets.put(incrementalKey.incrementAndGet(), new InfractionAgencyPair(t.getIssuingAgency(), t.getInfractionId()))
        );
        return KeyValueSource.fromMap(tickets);
    }

    @Override
    public void mapReduceJob(KeyValueSource<Long, InfractionAgencyPair> keyValueSource) throws ExecutionException, InterruptedException{

        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, InfractionAgencyPair> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<SortedSet<InfractionAgencyTicketCount>> future = job
                .mapper(new TotalTicketsByInfractionAndAgencyMapperAlternative(INFRACTION_MAP, AGENCY_SET))
                .combiner(new TotalTicketsByInfractionAndAgencyCombinerFactory())
                .reducer(new TotalTicketsByInfractionAndAgencyReducerFactory())
                .submit(new TotalTicketsByInfractionAndAgencyCollator(hazelcastInstance.getMap(INFRACTION_MAP)));


        SortedSet<InfractionAgencyTicketCount> set = future.get();

        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, set);
    }

    public static void main(String[] args){
        new Query1Client().executeQuery();
    }
}
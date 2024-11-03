package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyCollator;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyCombinerFactory;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyMapper;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyReducerFactory;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query1Client extends QueryClient<Long, InfractionAgency> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-ticket-count";
    private static final String[] OUT_CSV_HEADERS = {"Infraction", "Agency", "Tickets"};
    private static final String OUT_CSV_FILENAME = "/query1.csv";
    private static final String OUT_TIME_FILENAME = "/time1.txt";

    private Set<String> agencies;
    private Map<String, String> infractions;


    public Query1Client() {
        super(new QueryPropertiesFactory().build(), OUT_TIME_FILENAME);
    }

    @Override
    public KeyValueSource<Long, InfractionAgency> loadData(){
        agencies = getAgencySet();
        infractions = getInfractionsMap();

        AtomicLong incrementalKey = new AtomicLong();
        IMap<Long, InfractionAgency> tickets = hazelcastInstance.getMap(TICKET_MAP);
        tickets.clear();
        csvParserFactory.getTicketFileParser().consumeAll( t ->
                tickets.put(incrementalKey.incrementAndGet(), new InfractionAgency(t.getIssuingAgency(), t.getInfractionId()))
        );
        return KeyValueSource.fromMap(tickets);
    }

    @Override
    public void mapReduceJob(KeyValueSource<Long, InfractionAgency> keyValueSource) throws ExecutionException, InterruptedException{

        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, InfractionAgency> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<SortedSet<InfractionAgencyTicketCount>> future = job
                .mapper(new TotalTicketsByInfractionAndAgencyMapper(infractions, agencies))
                .combiner(new TotalTicketsByInfractionAndAgencyCombinerFactory())
                .reducer(new TotalTicketsByInfractionAndAgencyReducerFactory())
                .submit(new TotalTicketsByInfractionAndAgencyCollator(infractions));

        SortedSet<InfractionAgencyTicketCount> set = future.get();
        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, set);
    }

    public static void main(String[] args){
        new Query1Client().executeQuery();
    }
}

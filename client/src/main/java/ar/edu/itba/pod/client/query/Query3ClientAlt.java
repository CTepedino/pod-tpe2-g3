package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.CountyPercent;
import ar.edu.itba.pod.api.model.dto.PlateDateCounty;
import ar.edu.itba.pod.api.query3.alt.RepeatOffendersPercentCollator;
import ar.edu.itba.pod.api.query3.alt.RepeatOffendersPercentCombinerFactory;
import ar.edu.itba.pod.api.query3.alt.RepeatOffendersPercentMapper;
import ar.edu.itba.pod.api.query3.alt.RepeatOffendersPercentReducerFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3ClientAlt extends QueryClient<PlateDateCounty> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-repeat-offender-percent";
    private static final String[] OUT_CSV_HEADERS = {"County", "Percentage"};
    private static final String OUT_CSV_FILENAME = "/query3.csv";
    private static final String OUT_TIME_FILENAME = "/time3.txt";
    private static final int MINIMUM_N = 2;

    public Query3ClientAlt(){
        super(new QueryPropertiesFactory()
                        .useN(MINIMUM_N)
                        .useDateRange()
                        .build(),
                OUT_TIME_FILENAME);
    }

    @Override
    KeyValueSource<Long, PlateDateCounty> loadData() {
        return loadTicketData(t -> new PlateDateCounty(t.getPlate(), t.getIssueDate(), t.getCountyName()));
    }

    @Override
    void mapReduceJob(KeyValueSource<Long, PlateDateCounty> keyValueSource) throws ExecutionException, InterruptedException, IOException {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, PlateDateCounty> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<SortedSet<CountyPercent>> future = job
                .mapper(new RepeatOffendersPercentMapper(properties.getFrom(), properties.getTo()))
                .combiner(new RepeatOffendersPercentCombinerFactory())
                .reducer(new RepeatOffendersPercentReducerFactory())
                .submit(new RepeatOffendersPercentCollator(properties.getN()));

        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, future.get());
    }

    public static void main(String[] args){
        new Query3ClientAlt().executeQuery();
    }
}
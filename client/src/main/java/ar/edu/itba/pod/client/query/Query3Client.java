package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.CountyPercent;
import ar.edu.itba.pod.api.model.dto.InfractionPlateDateCounty;
import ar.edu.itba.pod.api.model.dto.PlateCounty;
import ar.edu.itba.pod.api.query3.count.RepeatOffendersCountCombinerFactory;
import ar.edu.itba.pod.api.query3.count.RepeatOffendersCountMapper;
import ar.edu.itba.pod.api.query3.count.RepeatOffendersCountReducerFactory;
import ar.edu.itba.pod.api.query3.percent.RepeatOffendersPercentCollator;
import ar.edu.itba.pod.api.query3.percent.RepeatOffendersPercentCombinerFactory;
import ar.edu.itba.pod.api.query3.percent.RepeatOffendersPercentMapper;
import ar.edu.itba.pod.api.query3.percent.RepeatOffendersPercentReducerFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3Client extends QueryClient<InfractionPlateDateCounty>{
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-repeat-offender-count";
    private static final String JOB_2_TRACKER_NAME = GROUP_NAME + "repeat-offender-percent";
    private static final String[] OUT_CSV_HEADERS = {"County", "Percentage"};
    private static final String OUT_CSV_FILENAME = "/query3.csv";
    private static final String OUT_TIME_FILENAME = "/time3.txt";
    private static final int MINIMUM_N = 2;
    private static final String TICKET_COUNT_MAP = GROUP_NAME + "-ticket-count";

    public Query3Client(){
        super(new QueryPropertiesFactory()
                        .useN(MINIMUM_N)
                        .useDateRange()
                        .build(),
                OUT_TIME_FILENAME);
    }


    @Override
    KeyValueSource<Long, InfractionPlateDateCounty> loadData() {
        return loadTicketData(t -> new InfractionPlateDateCounty(t.getPlate(), t.getIssueDate(), t.getCountyName(), t.getInfractionId()));
    }

    @Override
    void mapReduceJob(KeyValueSource<Long, InfractionPlateDateCounty> keyValueSource) throws ExecutionException, InterruptedException, IOException {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, InfractionPlateDateCounty> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<Map<PlateCounty, Map<String, Long>>> future = job
                .mapper(new RepeatOffendersCountMapper(properties.getFrom(), properties.getTo()))
                .combiner(new RepeatOffendersCountCombinerFactory())
                .reducer(new RepeatOffendersCountReducerFactory())
                .submit();

        IMap<PlateCounty, Map<String, Long>> repeatOffendersCount = hazelcastInstance.getMap(TICKET_COUNT_MAP);
        repeatOffendersCount.putAll(future.get());

        queryTimer.endJob();
        queryTimer.startJob();

        KeyValueSource<PlateCounty, Map<String, Long>> keyValueSource2 = KeyValueSource.fromMap(repeatOffendersCount);
        JobTracker job2Tracker = hazelcastInstance.getJobTracker(JOB_2_TRACKER_NAME);
        Job<PlateCounty, Map<String, Long>> job2 = job2Tracker.newJob(keyValueSource2);
        ICompletableFuture<SortedSet<CountyPercent>> future2 = job2
                .mapper(new RepeatOffendersPercentMapper(properties.getN()))
                .combiner(new RepeatOffendersPercentCombinerFactory())
                .reducer(new RepeatOffendersPercentReducerFactory())
                .submit(new RepeatOffendersPercentCollator());

        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, future2.get());
    }

    public static void main(String[] args){
        new Query3Client().executeQuery();
    }
}

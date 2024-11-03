package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.*;
import ar.edu.itba.pod.api.query3.alternative.*;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.client.HazelcastClient;
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
public class Query3AlternativeClient extends QueryClient<PlateDateCounty> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-repeat-offender-percent-a";
    private static final String[] OUT_CSV_HEADERS = {"County", "Percentage"};
    private static final String OUT_CSV_FILENAME = "/query3a.csv";
    private static final String OUT_TIME_FILENAME = "/time3a.txt";
    private static final int MINIMUM_N = 2;
    private static final String TICKET_COUNT_MAP = GROUP_NAME + "-ticket-count";

    public Query3AlternativeClient(){
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

        queryTimer.startJob();

        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, PlateDateCounty> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<Map<PlateCounty, Long>> future = job
                .mapper(new RepeatOffendersCountMapper(properties.getFrom(), properties.getTo()))
                .combiner(new RepeatOffendersCountCombinerFactory())
                .reducer(new RepeatOffendersCountReducerFactory())
                .submit();

        IMap<PlateCounty, Long> ticketsCountImap =  hazelcastInstance.getMap(TICKET_COUNT_MAP);
        ticketsCountImap.clear();
        ticketsCountImap.putAll(future.get());

        queryTimer.endJob();

        KeyValueSource<PlateCounty, Long> secondKeyValueSource = KeyValueSource.fromMap(ticketsCountImap);

        queryTimer.startJob();
        Job<PlateCounty, Long> secondJob = jobTracker.newJob(secondKeyValueSource);
        ICompletableFuture<SortedSet<CountyPercent>> secondFuture = secondJob
                .mapper(new RepeaterPercentMapper(properties.getN()))
                .combiner(new RepeaterPercentCombinerFactory())
                .reducer(new RepeaterPercentReducerFactory())
                .submit(new RepeaterPercentCollator());

        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, secondFuture.get());

        queryTimer.endJob();
    }

    @Override
    public void executeQuery() {
        try {
            queryTimer.startLoad();
            KeyValueSource<Long, PlateDateCounty> keyValueSource = loadData();
            queryTimer.endLoad();

            mapReduceJob(keyValueSource);
            queryTimer.endQuery();

        } catch (ExecutionException | InterruptedException e){
            logger.error("Hazelcast error: {}", e.getMessage());
        } catch (IOException e){
            logger.error("Error writing time file: {}", e.getMessage());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    public static void main(String[] args){
        new Query3AlternativeClient().executeQuery();
    }
}

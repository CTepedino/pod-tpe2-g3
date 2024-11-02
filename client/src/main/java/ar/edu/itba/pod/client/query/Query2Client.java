package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.api.model.dto.AgencyYearMonthYTD;
import ar.edu.itba.pod.api.query2.YTDByAgencyCollator;
import ar.edu.itba.pod.api.query2.YTDByAgencyCombinerFactory;
import ar.edu.itba.pod.api.query2.YTDByAgencyMapper;
import ar.edu.itba.pod.api.query2.YTDByAgencyReducerFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.Job;


import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query2Client extends QueryClient<Long, Ticket> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-agency-ytd";
    private static final String[] OUT_CSV_HEADERS = {"Agency", "Year", "Month", "YTD"};
    private static final String OUT_CSV_FILENAME = "/query2.csv";
    private static final String OUT_TIME_FILENAME = "/time2.txt";

    public Query2Client() {
        super(new QueryPropertiesFactory().build(), OUT_TIME_FILENAME);
    }

    @Override
    public KeyValueSource<Long, Ticket> loadData(){
        fillAgencyList();
        AtomicLong incrementalKey = new AtomicLong();
        IMap<Long, Ticket> tickets = hazelcastInstance.getMap(TICKET_MAP);
        tickets.clear();
        csvParserFactory.getTicketFileParser().consumeAll( t ->
                tickets.put(incrementalKey.incrementAndGet(), t)
        );
        return KeyValueSource.fromMap(tickets);
    }

    @Override
    public void mapReduceJob(KeyValueSource<Long, Ticket> keyValueSource) throws ExecutionException, InterruptedException{

        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, Ticket> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<SortedSet<AgencyYearMonthYTD>> future = job
                .mapper(new YTDByAgencyMapper())
                .combiner(new YTDByAgencyCombinerFactory())
                .reducer(new YTDByAgencyReducerFactory())
                .submit(new YTDByAgencyCollator());

        SortedSet<AgencyYearMonthYTD> set = future.get();
        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, set);
    }

    public static void main(String[] args) {
        new Query2Client().executeQuery();
    }

}

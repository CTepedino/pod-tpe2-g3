package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.Job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("deprecation")
public class Query2Client extends QueryClient<Long, Ticket> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-agency-ytd";
    private static final String[] OUT_CSV_HEADERS = {};
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
    }

    public static void main(String[] args) {
        new Query2Client().executeQuery();
    }

}

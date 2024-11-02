package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3Client extends QueryClient<Long, Ticket> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-repeat-offender-percent";
    private static final String[] OUT_CSV_HEADERS = {"County", "Percentage"};
    private static final String OUT_CSV_FILENAME = "/query3.csv";
    private static final String OUT_TIME_FILENAME = "/time3.txt";
    private static final int MINIMUM_N = 2;

    public Query3Client(){
        super(new QueryPropertiesFactory()
                .useN(MINIMUM_N)
                .useDateRange()
                .build(),
            OUT_TIME_FILENAME);
    }

    @Override
    KeyValueSource<Long, Ticket> loadData() {
        return null;
    }

    @Override
    void mapReduceJob(KeyValueSource<Long, Ticket> keyValueSource) throws ExecutionException, InterruptedException {

    }

    public static void main(String[] args){
        new Query3Client().executeQuery();
    }
}

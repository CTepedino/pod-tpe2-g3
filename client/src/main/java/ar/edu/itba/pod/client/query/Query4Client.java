package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query4Client extends QueryClient<Long, Ticket>{
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-top-infractions";
    private static final String[] OUT_CSV_HEADERS = {"Infraction", "Max", "Min", "Diff"};
    private static final String OUT_CSV_FILENAME = "/query4.csv";
    private static final String OUT_TIME_FILENAME = "/time4.txt";
    private static final int MINIMUM_N = 1;

    public Query4Client(){
        super(new QueryPropertiesFactory()
                .useN(MINIMUM_N)
                .useAgency()
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
        new Query4Client().executeQuery();
    }
}

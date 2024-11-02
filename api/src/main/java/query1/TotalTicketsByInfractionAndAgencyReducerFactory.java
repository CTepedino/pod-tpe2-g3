package query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyReducerFactory implements ReducerFactory<InfractionAgencyPair, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(InfractionAgencyPair infractionAgencyPair) {
        return new TotalTicketsByInfractionAndAgencyReducer();
    }

    private static class TotalTicketsByInfractionAndAgencyReducer extends Reducer<Long, Long> {
        private Long sum = 0L;

        @Override
        public void reduce(Long value){
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}

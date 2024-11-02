package ar.edu.itba.pod.api.query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyReducerFactory implements ReducerFactory<InfractionAgency, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(InfractionAgency infractionAgency) {
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

package ar.edu.itba.pod.api.query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyCombinerFactory implements CombinerFactory<InfractionAgencyPair, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(InfractionAgencyPair infractionAgencyPair) {
        return new TotalTicketsByInfractionAndAgencyCombiner();
    }

    private static class TotalTicketsByInfractionAndAgencyCombiner extends Combiner<Long, Long> {
        private Long sum = 0L;

        @Override
        public void combine(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0L;
        }
    }
}

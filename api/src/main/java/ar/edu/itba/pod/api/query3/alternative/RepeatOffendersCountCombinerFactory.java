package ar.edu.itba.pod.api.query3.alternative;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountCombinerFactory implements CombinerFactory<PlateCounty, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(PlateCounty key) {
        return new RepeatOffendersCountCombiner();
    }

    private static class RepeatOffendersCountCombiner extends Combiner<Long, Long>{
        private Long count = 0L;

        @Override
        public void combine(Long partial) {
            count += partial;
        }

        @Override
        public Long finalizeChunk() {
            return count;
        }

        @Override
        public void reset() {
            count = 0L;
        }
    }
}

package ar.edu.itba.pod.api.query3.alt;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentCombinerFactory implements CombinerFactory<String, String, Map<String, Long>> {
    @Override
    public Combiner<String, Map<String, Long>> newCombiner(String key) {
        return new RepeatOffendersPercentCombiner();
    }

    private static class RepeatOffendersPercentCombiner extends Combiner<String, Map<String, Long>>{
        Map<String, Long> count = new HashMap<>();
        private static final Long ONE = 1L;

        @Override
        public void combine(String key) {
            count.merge(key, ONE, Long::sum);
        }

        @Override
        public Map<String, Long> finalizeChunk() {
            return count;
        }

        @Override
        public void reset() {
            count = new HashMap<>();
        }
    }
}

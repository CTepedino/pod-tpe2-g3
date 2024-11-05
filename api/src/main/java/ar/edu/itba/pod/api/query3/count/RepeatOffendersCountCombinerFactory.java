package ar.edu.itba.pod.api.query3.count;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountCombinerFactory implements CombinerFactory<PlateCounty, String, Map<String, Long>> {
    @Override
    public Combiner<String, Map<String, Long>> newCombiner(PlateCounty plateCounty) {
        return new RepeatOffendersCountCombiner();
    }

    private static class RepeatOffendersCountCombiner extends Combiner<String, Map<String, Long>> {
        private static final Long ONE = 1L;

        Map<String, Long> infractions = new HashMap<>();

        @Override
        public void combine(String infraction) {
            infractions.merge(infraction, ONE, Long::sum);
        }

        @Override
        public Map<String, Long> finalizeChunk() {
            return infractions;
        }

        @Override
        public void reset() {
            infractions = new HashMap<>();
        }
    }
}

package ar.edu.itba.pod.api.query3.percent;

import ar.edu.itba.pod.api.model.dto.PlateCounter;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentCombinerFactory implements CombinerFactory<String, Boolean, PlateCounter> {
    @Override
    public Combiner<Boolean, PlateCounter> newCombiner(String key) {
        return new RepeatOffendersPercentCombiner();
    }

    private static class RepeatOffendersPercentCombiner extends Combiner<Boolean, PlateCounter> {
        PlateCounter plateCounter = new PlateCounter(0, 0);

        @Override
        public void combine(Boolean isRepeater) {
            plateCounter.increaseTotal(1);
            if (isRepeater){
                plateCounter.increaseRepeaters(1);
            }
        }

        @Override
        public PlateCounter finalizeChunk() {
            return plateCounter;
        }

        @Override
        public void reset() {
            plateCounter = new PlateCounter(0, 0);
        }
    }
}

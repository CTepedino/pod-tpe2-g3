package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.Range;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class TopNInfractionsByFineRangeCombinerFactory implements CombinerFactory<String, Double, Range> {
    @Override
    public Combiner<Double, Range> newCombiner(String s) {
        return new TopNInfractionsByFineRangeCombiner();
    }

    private static class TopNInfractionsByFineRangeCombiner extends Combiner<Double, Range> {
        double min = 0;
        double max = 0;

        @Override
        public void combine(Double fine) {
            if (Double.compare(fine, min) < 0){
                min = fine;
            }
            if (Double.compare(fine, max) > 0){
                max = fine;
            }
        }

        @Override
        public Range finalizeChunk() {
            return new Range(min, max);
        }

        @Override
        public void reset() {
            min = 0;
            max = 0;
        }
    }
}

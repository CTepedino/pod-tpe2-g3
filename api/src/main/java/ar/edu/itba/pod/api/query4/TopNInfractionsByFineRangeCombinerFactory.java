package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.Range;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeCombinerFactory implements CombinerFactory<String, Integer, Range> {
    @Override
    public Combiner<Integer, Range> newCombiner(String key) {
        return new TopNInfractionsByFineRangeCombiner();
    }

    private static class TopNInfractionsByFineRangeCombiner extends Combiner<Integer, Range> {
        Range range;

        @Override
        public void combine(Integer fine) {
            if (range == null){
                range = new Range(fine, fine);
            } else {
                if (fine < range.getValueMin()){
                    range.setValueMin(fine);
                } else if (fine > range.getValueMax()){
                    range.setValueMax(fine);
                }
            }
        }

        @Override
        public Range finalizeChunk() {
            return range;
        }

        @Override
        public void reset() {
            range = null;
        }
    }
}

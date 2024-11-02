package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.Range;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeReducerFactory implements ReducerFactory<String, Range, Range> {
    @Override
    public Reducer<Range, Range> newReducer(String key) {
        return new TopNInfractionsByFineRangeReducer();
    }

    private static class TopNInfractionsByFineRangeReducer extends Reducer<Range, Range> {
        Range greatestRange;

        @Override
        public void reduce(Range range) {
            if (greatestRange == null){
                greatestRange = range;
            } else {
                if (range.getValueMin() < greatestRange.getValueMin()){
                    greatestRange.setValueMin(range.getValueMin());
                }
                if (range.getValueMax() > greatestRange.getValueMax()){
                    greatestRange.setValueMax(range.getValueMax());
                }
            }
        }

        @Override
        public Range finalizeReduce() {
            return greatestRange;
        }
    }
}

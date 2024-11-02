package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.Range;
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
                if (Double.compare(range.getValueMin(), greatestRange.getValueMin()) < 0){
                    greatestRange.setValueMin(range.getValueMin());
                }
                if (Double.compare(range.getValueMax(), greatestRange.getValueMax()) > 0){
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

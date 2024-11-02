package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.Range;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeReducerFactory implements ReducerFactory<String, Double, Range> {
    @Override
    public Reducer<Double, Range> newReducer(String key) {
        return new TopNInfractionsByFineRangeReducer();
    }

    private static class TopNInfractionsByFineRangeReducer extends Reducer<Double, Range> {
        private double min = 0;
        private double max = 0;


        @Override
        public void reduce(Double fine) {
            if (Double.compare(fine, min) < 0){
                min = fine;
            }
            if (Double.compare(fine, max) > 0){
                max = fine;
            }
        }

        @Override
        public Range finalizeReduce() {
            return new Range(min, max);
        }
    }
}

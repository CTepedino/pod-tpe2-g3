package ar.edu.itba.pod.api.query3.percent;

import ar.edu.itba.pod.api.model.dto.PlateCounter;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentReducerFactory implements ReducerFactory<String, PlateCounter, PlateCounter> {
    @Override
    public Reducer<PlateCounter, PlateCounter> newReducer(String s) {
        return new RepeatOffendersPercentReducer();
    }

    private static class RepeatOffendersPercentReducer extends Reducer<PlateCounter, PlateCounter>{
        PlateCounter plateCounter = new PlateCounter(0, 0);

        @Override
        public void reduce(PlateCounter partialCount) {
            plateCounter.increaseTotal(partialCount.getTotal());
            plateCounter.increaseRepeaters(partialCount.getRepeaters());
        }

        @Override
        public PlateCounter finalizeReduce() {
            return plateCounter;
        }
    }
}

package ar.edu.itba.pod.api.query3.alternative;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountReducerFactory implements ReducerFactory<PlateCounty, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(PlateCounty plateCounty) {
        return new RepeatOffendersCountReducer();
    }

    private static class RepeatOffendersCountReducer extends Reducer<Long, Long>{
        private Long count = 0L;

        @Override
        public void reduce(Long partial) {
            count += partial;
        }

        @Override
        public Long finalizeReduce() {
            return count;
        }
    }
}

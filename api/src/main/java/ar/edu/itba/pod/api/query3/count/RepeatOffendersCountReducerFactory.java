package ar.edu.itba.pod.api.query3.count;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountReducerFactory implements ReducerFactory<PlateCounty, Map<String, Long>, List<Long>> {
    @Override
    public Reducer<Map<String, Long>, List<Long>> newReducer(PlateCounty plateCounty) {
        return new RepeatOffendersCountReducer();
    }

    private static class RepeatOffendersCountReducer extends Reducer<Map<String, Long>, List<Long>> {
        private final Map<String, Long> infractions = new HashMap<>();

        @Override
        public void reduce(Map<String, Long> partialInfractions) {
            partialInfractions.forEach( (key, value) ->
                infractions.merge(key, value, Long::sum)
            );
        }

        @Override
        public List<Long> finalizeReduce() {
            return new ArrayList<>(infractions.values());
        }
    }
}

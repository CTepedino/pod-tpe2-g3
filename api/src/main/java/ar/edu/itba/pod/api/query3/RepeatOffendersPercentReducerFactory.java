package ar.edu.itba.pod.api.query3;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentReducerFactory implements ReducerFactory<String, Map<String, Long>, Map<String, Long>> {
    @Override
    public Reducer<Map<String, Long>, Map<String, Long>> newReducer(String key) {
        return new RepeatOffendersPercentReducer();
    }

    private static class RepeatOffendersPercentReducer extends Reducer<Map<String, Long>, Map<String, Long>> {
        private final Map<String, Long> count = new HashMap<>();

        @Override
        public void reduce(Map<String, Long> partialCounts) {
            for(Map.Entry<String, Long> entry : partialCounts.entrySet()){
                count.merge(entry.getKey(), entry.getValue(), Long::sum);
            }
        }

        @Override
        public Map<String, Long> finalizeReduce() {
            return count;
        }
    }
}

package ar.edu.itba.pod.api.query3.alternative;

import ar.edu.itba.pod.api.model.dto.InfractionRepeaters;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class RepeaterPercentReducerFactory implements ReducerFactory<String, InfractionRepeaters, Double> {

    @Override
    public Reducer<InfractionRepeaters, Double> newReducer(String s) {
        return new RepeaterPercentReducer();
    }

    private static class RepeaterPercentReducer extends Reducer<InfractionRepeaters, Double> {
        private final InfractionRepeaters infractionRepeaters = new InfractionRepeaters(0, 0);

        @Override
        public void reduce(InfractionRepeaters partial) {
            infractionRepeaters.incrementTotal(partial.getTotalPlates());
            infractionRepeaters.incrementRepeaters(partial.getRepeaterPlates());
        }

        @Override
        public Double finalizeReduce() {
            return (double) infractionRepeaters.getRepeaterPlates() / infractionRepeaters.getTotalPlates() * 100;
        }

    }
}

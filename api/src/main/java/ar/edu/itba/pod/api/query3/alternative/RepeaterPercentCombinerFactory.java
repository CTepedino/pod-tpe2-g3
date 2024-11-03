package ar.edu.itba.pod.api.query3.alternative;

import ar.edu.itba.pod.api.model.dto.InfractionRepeaters;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class RepeaterPercentCombinerFactory implements CombinerFactory<String, Boolean, InfractionRepeaters> {

    @Override
    public Combiner<Boolean, InfractionRepeaters> newCombiner(String key) {
        return new RepeaterPercentCombiner();
    }

    private static class RepeaterPercentCombiner extends Combiner<Boolean, InfractionRepeaters>{
        private InfractionRepeaters infractionRepeaters = new InfractionRepeaters(0, 0);


        @Override
        public void combine(Boolean isRepeater) {
            infractionRepeaters.incrementTotal(1);
            if (isRepeater){
                infractionRepeaters.incrementRepeaters(1);
            }
        }

        @Override
        public InfractionRepeaters finalizeChunk() {
            return infractionRepeaters;
        }

        @Override
        public void reset() {
            infractionRepeaters = new InfractionRepeaters(0, 0);
        }
    }
}

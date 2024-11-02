package ar.edu.itba.pod.api.query2;

import ar.edu.itba.pod.api.model.dto.AgencyYearPair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class YTDByAgencyCombinerFactory implements CombinerFactory<AgencyYearPair, Integer[], Integer[]> {
    @Override
    public Combiner<Integer[], Integer[]> newCombiner(AgencyYearPair agencyYearPair) {
        return new YTDByAgencyCombiner();
    }

    private static class YTDByAgencyCombiner extends Combiner<Integer[], Integer[]>{
        private final Integer[] monthlyYTD = new Integer[12];

        @Override
        public void beginCombine() {
            Arrays.fill(monthlyYTD,0.0);
        }

        @Override
        public void combine(Integer[] value) {
            int startIndex = value[0] - 1;
            for(int i = startIndex; i < 12; i++){
                monthlyYTD[i] += value[1];
            }
        }

        @Override
        public Integer[] finalizeChunk() {
            return monthlyYTD;
        }

        @Override
        public void reset() {
            Arrays.fill(monthlyYTD,0);
        }
    }
}

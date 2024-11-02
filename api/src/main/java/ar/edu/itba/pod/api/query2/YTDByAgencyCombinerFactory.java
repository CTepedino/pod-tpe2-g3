package ar.edu.itba.pod.api.query2;

import ar.edu.itba.pod.api.model.dto.AgencyYearPair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class YTDByAgencyCombinerFactory implements CombinerFactory<AgencyYearPair, Double[], Double[]> {
    @Override
    public Combiner<Double[], Double[]> newCombiner(AgencyYearPair agencyYearPair) {
        return new YTDByAgencyCombiner();
    }

    private static class YTDByAgencyCombiner extends Combiner<Double[], Double[]>{
        private Double[] monthlyYTD = new Double[12];

        @Override
        public void beginCombine() {
            Arrays.fill(monthlyYTD,0.0);
        }

        @Override
        public void combine(Double[] value) {
            int startIndex = ((int) value[0].doubleValue()) - 1;
            for(int i = startIndex; i < 12; i++){
                monthlyYTD[i] += value[1];
            }
        }

        @Override
        public Double[] finalizeChunk() {
            return monthlyYTD;
        }

        @Override
        public void reset() {
            Arrays.fill(monthlyYTD,0.0);
        }
    }
}

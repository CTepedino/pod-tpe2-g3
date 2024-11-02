package query2;

import ar.edu.itba.pod.api.model.dto.AgencyYearPair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.Arrays;


@SuppressWarnings("deprecation")
public class YTDByAgencyReducerFactory implements ReducerFactory<AgencyYearPair, Double[], Double[]> {
    @Override
    public Reducer<Double[], Double[]> newReducer(AgencyYearPair key) {
        return new YTDByAgencyReducer();
    }

    private static class YTDByAgencyReducer extends Reducer<Double[], Double[]>{
        private Double[] monthlyYTD = new Double[12];

        @Override
        public void beginReduce() {
            Arrays.fill(monthlyYTD,0.0);
        }

        @Override
        public void reduce(Double[] value){
            int startIndex = ((int) value[0].doubleValue()) - 1;
            for(int i = startIndex; i < 12; i++){
                monthlyYTD[i] += value[1];
            }
        }

        @Override
        public Double[] finalizeReduce(){
            return monthlyYTD;
        }

    }
}

package ar.edu.itba.pod.api.query2;

import ar.edu.itba.pod.api.model.dto.AgencyYear;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.Arrays;


@SuppressWarnings("deprecation")
public class YTDByAgencyReducerFactory implements ReducerFactory<AgencyYear, Integer[], Integer[]> {
    @Override
    public Reducer<Integer[], Integer[]> newReducer(AgencyYear key) {
        return new YTDByAgencyReducer();
    }

    private static class YTDByAgencyReducer extends Reducer<Integer[], Integer[]>{
        private final Integer[] monthlyYTD = new Integer[12];

        @Override
        public void beginReduce() {
            Arrays.fill(monthlyYTD,0);
        }

        @Override
        public void reduce(Integer[] value){
            int startIndex = value[0] - 1;
            for(int i = startIndex; i < 12; i++){
                monthlyYTD[i] += value[1];
            }
        }

        @Override
        public Integer[] finalizeReduce(){
            return monthlyYTD;
        }

    }
}

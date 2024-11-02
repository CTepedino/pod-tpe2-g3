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

    private static class YTDByAgencyReducer extends Reducer<Integer[], Integer[]> {
        private static final int MONTH_COUNT = 12;
        Integer[] monthTotal;

        private YTDByAgencyReducer(){
            monthTotal = new Integer[MONTH_COUNT];
            Arrays.fill(monthTotal, 0);
        }

        @Override
        public void reduce(Integer[] monthPartial) {
            for (int i = 0; i < monthTotal.length; i++){
                monthTotal[i] += monthPartial[i];
            }
        }

        @Override
        public Integer[] finalizeReduce() {
            return monthTotal;
        }
    }
}

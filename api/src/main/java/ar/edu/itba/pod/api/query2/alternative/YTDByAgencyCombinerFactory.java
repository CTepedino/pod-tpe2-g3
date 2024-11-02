package ar.edu.itba.pod.api.query2.alternative;

import ar.edu.itba.pod.api.model.dto.AgencyYear;
import ar.edu.itba.pod.api.model.dto.MonthFine;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class YTDByAgencyCombinerFactory implements CombinerFactory<AgencyYear, MonthFine, Integer[]> {
    @Override
    public Combiner<MonthFine, Integer[]> newCombiner(AgencyYear key) {
        return new YTDByAgencyCombiner();
    }

    private static class YTDByAgencyCombiner extends Combiner<MonthFine, Integer[]> {
        private static final int MONTH_COUNT = 12;
        private Integer[] monthTotal;

        private YTDByAgencyCombiner(){
            monthTotal = new Integer[MONTH_COUNT];
            Arrays.fill(monthTotal, 0);
        }

        @Override
        public void combine(MonthFine monthFine) {
            monthTotal[monthFine.getMonth().getValue()-1] += monthFine.getFine();
        }

        @Override
        public Integer[] finalizeChunk() {
            return monthTotal;
        }

        @Override
        public void reset() {
            monthTotal = new Integer[MONTH_COUNT];
            Arrays.fill(monthTotal, 0);
        }
    }
}

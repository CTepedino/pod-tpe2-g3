package ar.edu.itba.pod.api.query3.percent;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Collection;
import java.util.List;


@SuppressWarnings("deprecation")
public class RepeatOffendersPercentMapper implements Mapper<PlateCounty, List<Long>, String, Boolean> {
    private final int minRepeats;

    public RepeatOffendersPercentMapper(int minRepeats) {
        this.minRepeats = minRepeats;
    }

    @Override
    public void map(PlateCounty key, List<Long> value, Context<String, Boolean> context) {
        boolean isRepeater = false;
        for (Long count : value){
            if (count >= minRepeats){
                isRepeater = true;
                break;
            }
        }

        context.emit(key.getCounty(), isRepeater);
    }
}

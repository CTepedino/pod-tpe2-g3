package ar.edu.itba.pod.api.query3;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;


@SuppressWarnings("deprecation")
public class RepeaterPercentMapper implements Mapper<PlateCounty, Long, String, Boolean> {
    private final int minRepeats;

    public RepeaterPercentMapper(int minRepeats){
        this.minRepeats = minRepeats;
    }

    @Override
    public void map(PlateCounty key, Long value, Context<String,Boolean> context) {
        context.emit(key.getCounty(), value >= minRepeats);
    }
}

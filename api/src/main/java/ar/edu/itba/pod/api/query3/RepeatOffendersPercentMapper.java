package ar.edu.itba.pod.api.query3;

import ar.edu.itba.pod.api.model.dto.PlateDateCounty;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDate;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentMapper implements Mapper<Long, PlateDateCounty, String, String>{

    private final LocalDate from;
    private final LocalDate to;

    public RepeatOffendersPercentMapper(LocalDate from, LocalDate to){
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Long key, PlateDateCounty value, Context<String,String> context) {
        if (value.getIssueDate().isAfter(from) && value.getIssueDate().isBefore(to)){
            context.emit(value.getCounty(), value.getPlate());
        }
    }

}

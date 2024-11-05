package ar.edu.itba.pod.api.query3.count;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import ar.edu.itba.pod.api.model.dto.InfractionPlateDateCounty;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDate;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountMapper implements Mapper<Long, InfractionPlateDateCounty, PlateCounty, String> {
    private static final Long ONE = 1L;

    private final LocalDate from;
    private final LocalDate to;

    public RepeatOffendersCountMapper(LocalDate from, LocalDate to){
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Long key, InfractionPlateDateCounty value, Context<PlateCounty,String> context) {
        if (value.getIssueDate().isAfter(from) && value.getIssueDate().isBefore(to)){
            context.emit(new PlateCounty(value.getPlate(), value.getCounty()), value.getInfraction());
        }
    }

}

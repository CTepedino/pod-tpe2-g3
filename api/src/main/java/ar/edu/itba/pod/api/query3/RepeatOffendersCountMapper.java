package ar.edu.itba.pod.api.query3;

import ar.edu.itba.pod.api.model.dto.PlateCounty;
import ar.edu.itba.pod.api.model.dto.PlateDateCounty;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDate;

@SuppressWarnings("deprecation")
public class RepeatOffendersCountMapper implements Mapper<Long, PlateDateCounty, PlateCounty, Long> {
    private static final Long ONE = 1L;

    private final LocalDate from;
    private final LocalDate to;

    public RepeatOffendersCountMapper(LocalDate from, LocalDate to){
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Long key, PlateDateCounty value, Context<PlateCounty,Long> context) {
        if (value.getIssueDate().isAfter(from) && value.getIssueDate().isBefore(to)){
            context.emit(new PlateCounty(value.getPlate(), value.getCounty()), ONE);
        }
    }

}

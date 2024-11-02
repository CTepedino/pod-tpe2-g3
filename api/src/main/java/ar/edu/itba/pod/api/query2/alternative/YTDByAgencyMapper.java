package ar.edu.itba.pod.api.query2.alternative;

import ar.edu.itba.pod.api.model.dto.AgencyDateFine;
import ar.edu.itba.pod.api.model.dto.AgencyYear;
import ar.edu.itba.pod.api.model.dto.MonthFine;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Set;

@SuppressWarnings("deprecation")
public class YTDByAgencyMapper implements Mapper<Long, AgencyDateFine, AgencyYear, MonthFine>{
    private final Set<String> agencySet;

    public YTDByAgencyMapper(Set<String> agencySet){
        this.agencySet = agencySet;
    }

    @Override
    public void map(Long key, AgencyDateFine value, Context<AgencyYear, MonthFine> context) {
        if (agencySet.contains(value.getAgency())){
            context.emit(new AgencyYear(value.getAgency(), value.getDate().getYear()), new MonthFine(value.getDate().getMonth(), value.getFine()));
        }
    }
}

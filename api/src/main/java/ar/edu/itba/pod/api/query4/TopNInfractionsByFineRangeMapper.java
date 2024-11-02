package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyFine;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeMapper implements Mapper<Long, InfractionAgencyFine, String, Double> {
    private final String agency;

    public TopNInfractionsByFineRangeMapper(String agency) {
        this.agency = agency;
    }

    @Override
    public void map(Long key, InfractionAgencyFine value, Context<String, Double> context) {
        if (value.getAgency().equals(agency)){
            context.emit(value.getInfractionId(), value.getFine());
        }
    }
}

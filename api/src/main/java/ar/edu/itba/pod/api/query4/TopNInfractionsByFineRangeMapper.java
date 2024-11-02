package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyFine;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeMapper implements Mapper<Long, InfractionAgencyFine, String, Integer> {
    private final String agency;
    private final Map<String, String> infractions;

    public TopNInfractionsByFineRangeMapper(String agency, Map<String, String> infractions) {
        this.agency = agency;
        this.infractions = infractions;
    }

    @Override
    public void map(Long key, InfractionAgencyFine value, Context<String, Integer> context) {
        if (value.getAgency().equals(agency) && infractions.containsKey(value.getInfractionId())){
            context.emit(value.getInfractionId(), value.getFine());
        }
    }
}

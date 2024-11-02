package query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyMapper implements Mapper<Long, InfractionAgencyPair, InfractionAgencyPair, Long> {
    private static final Long ONE = 1L;

    private final Map<String, String> infractionMap;
    private final Set<String> agencySet;


    public TotalTicketsByInfractionAndAgencyMapper(Map<String, String> infractionMap, Set<String> agencySet){
        this.infractionMap = infractionMap;
        this.agencySet = agencySet;
    }

    @Override
    public void map(Long key, InfractionAgencyPair value, Context<InfractionAgencyPair, Long> context) {
        if (infractionMap.containsKey(value.getInfractionId()) && agencySet.contains(value.getAgency())) {
            context.emit(value, ONE);
        }
    }

}
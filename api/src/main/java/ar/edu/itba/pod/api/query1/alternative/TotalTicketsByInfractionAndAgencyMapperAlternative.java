package ar.edu.itba.pod.api.query1.alternative;

import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyMapperAlternative implements Mapper<Long, InfractionAgency, InfractionAgency, Long>, HazelcastInstanceAware {
    private static final Long ONE = 1L;
    private transient HazelcastInstance hazelcastInstance;

    private final String infractionMapName;
    private final String agencySetName;

    private IMap<String, String> infractionMap;
    private ISet<String> agencySet;

    public TotalTicketsByInfractionAndAgencyMapperAlternative(String infractionMapName, String agencySetName){
        this.infractionMapName = infractionMapName;
        this.agencySetName = agencySetName;
    }

    @Override
    public void map(Long key, InfractionAgency value, Context<InfractionAgency, Long> context) {
        if (infractionMap == null || agencySet == null) {
            infractionMap = hazelcastInstance.getMap(infractionMapName);
            agencySet = hazelcastInstance.getSet(agencySetName);
        }

        if (infractionMap.containsKey(value.getInfractionId()) && agencySet.contains(value.getAgency())) {
            context.emit(value, ONE);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}

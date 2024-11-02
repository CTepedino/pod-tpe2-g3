package ar.edu.itba.pod.api.query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyMapperAlternative implements Mapper<Long, InfractionAgencyPair, InfractionAgencyPair, Long>, HazelcastInstanceAware {
    private static final Long ONE = 1L;
    private transient HazelcastInstance hazelcastInstance;

    private final String infractionMapName;
    private final String agencyListName;

    public TotalTicketsByInfractionAndAgencyMapperAlternative(String infractionMapName, String agencyListName){
        this.infractionMapName = infractionMapName;
        this.agencyListName = agencyListName;
    }

    @Override
    public void map(Long key, InfractionAgencyPair value, Context<InfractionAgencyPair, Long> context) {
        var infractions = hazelcastInstance.getMap(infractionMapName);
        var agencies = hazelcastInstance.getSet(agencyListName);

        if (infractions.containsKey(value.getInfractionId()) && agencies.contains(value.getAgency())) {
            context.emit(value, ONE);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

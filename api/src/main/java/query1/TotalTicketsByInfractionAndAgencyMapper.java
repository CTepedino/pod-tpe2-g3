package query1;

import ar.edu.itba.pod.api.model.Infraction;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyMapper implements Mapper<Long, InfractionAgencyPair, InfractionAgencyPair, Long>, HazelcastInstanceAware {
    private static final Long ONE = 1L;
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(Long key, InfractionAgencyPair value, Context<InfractionAgencyPair, Long> context) {
        var infractions = hazelcastInstance.getMap("g3-infractions");
        var agencies = hazelcastInstance.getList("g3-agencies");

        if (infractions.containsKey(value.getInfractionId()) && agencies.contains(value.getAgency())) {
            context.emit(value, ONE);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

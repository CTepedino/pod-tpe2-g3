package query1;

import ar.edu.itba.pod.api.model.Infraction;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class TicketsByInfractionMapper implements Mapper<Long, Infraction, String, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long aLong, Infraction infraction, Context<String, Long> context) {
        context.emit(infraction.getId(), ONE);
    }
}

package ar.edu.itba.pod.api.query2;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.api.model.dto.AgencyYearPair;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class YTDByAgencyMapper implements Mapper<Long, Ticket, AgencyYearPair, Double[]>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(Long key, Ticket value, Context<AgencyYearPair, Double[]> context) {
        var agencies = hazelcastInstance.getList("g3-agencies");
        Double[] monthAndAmount = new Double[2];

        if(agencies.contains(value.getIssuingAgency())){
            monthAndAmount[0] = (double) value.getIssueDate().getMonthValue() - 1;
            monthAndAmount[1] = value.getFineAmount();
            context.emit(new AgencyYearPair(value.getIssuingAgency(), value.getIssueDate().getYear()), monthAndAmount);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

package ar.edu.itba.pod.api.query2;

import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.api.model.dto.AgencyYear;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class YTDByAgencyMapper implements Mapper<Long, Ticket, AgencyYear, Integer[]>, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void map(Long key, Ticket value, Context<AgencyYear, Integer[]> context) {
        var agencies = hazelcastInstance.getList("g3-agencies");
        Integer[] monthAndAmount = new Integer[2];

        if(agencies.contains(value.getIssuingAgency())){
            monthAndAmount[0] = value.getIssueDate().getMonthValue() - 1;
            monthAndAmount[1] = value.getFineAmount();
            context.emit(new AgencyYear(value.getIssuingAgency(), value.getIssueDate().getYear()), monthAndAmount);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

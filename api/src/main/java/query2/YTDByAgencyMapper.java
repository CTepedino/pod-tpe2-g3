package query2;

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
        Double[] monthlyAmount = new Double[12];

        if(agencies.contains(value.getIssuingAgency())){
            monthlyAmount[value.getIssueDate().getMonthValue() - 1] = value.getFineAmount();
            context.emit(new AgencyYearPair(value.getIssuingAgency(), value.getIssueDate().getYear()), monthlyAmount);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

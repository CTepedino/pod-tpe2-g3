package query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgencyPair;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyCollator implements Collator<Map.Entry<InfractionAgencyPair, Long>, SortedSet<InfractionAgencyTicketCount>>, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public SortedSet<InfractionAgencyTicketCount> collate(Iterable<Map.Entry<InfractionAgencyPair, Long>> values) {
        Map<String, String> infractions = hazelcastInstance.getMap("g3-infractions");

        SortedSet<InfractionAgencyTicketCount> set = new TreeSet<>(
                (count1, count2) -> {
                    if (count1.getTicketCount() > count2.getTicketCount()) {
                        return 1;
                    } else if (count1.getTicketCount() == count2.getTicketCount()) {
                        int c;
                        if ((c = count1.getInfraction().compareTo(count2.getInfraction())) != 0) {
                            return c;
                        } else {
                            return count1.getAgency().compareTo(count2.getAgency());
                        }
                    }
                    return -1;
                }
        );

        for (Map.Entry<InfractionAgencyPair, Long> entry : values){
            set.add(new InfractionAgencyTicketCount(
                    infractions.get(entry.getKey().getInfractionId()),
                    entry.getKey().getAgency(),
                    entry.getValue()
            ));
        }

        return set;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}

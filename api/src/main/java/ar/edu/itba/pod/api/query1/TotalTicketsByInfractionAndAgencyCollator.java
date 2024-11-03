package ar.edu.itba.pod.api.query1;

import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


@SuppressWarnings("deprecation")
public class TotalTicketsByInfractionAndAgencyCollator implements Collator<Map.Entry<InfractionAgency, Long>, SortedSet<InfractionAgencyTicketCount>> {
    private final Map<String, String> infractions;

    public TotalTicketsByInfractionAndAgencyCollator(Map<String, String> infractions){
        this.infractions = infractions;
    }

    @Override
    public SortedSet<InfractionAgencyTicketCount> collate(Iterable<Map.Entry<InfractionAgency, Long>> values) {

        SortedSet<InfractionAgencyTicketCount> set = new TreeSet<>(
                (count1, count2) -> {
                    if (count1.getTicketCount() < count2.getTicketCount()) {
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

        for (Map.Entry<InfractionAgency, Long> entry : values){
            set.add(new InfractionAgencyTicketCount(
                    infractions.get(entry.getKey().getInfractionId()),
                    entry.getKey().getAgency(),
                    entry.getValue()
            ));
        }

        return set;
    }

}

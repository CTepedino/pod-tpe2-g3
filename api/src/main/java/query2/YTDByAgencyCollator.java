package query2;

import ar.edu.itba.pod.api.model.dto.AgencyYearMonthYTD;
import ar.edu.itba.pod.api.model.dto.AgencyYearPair;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class YTDByAgencyCollator implements Collator<Map.Entry<AgencyYearPair,Double[]>, SortedSet<AgencyYearMonthYTD>> {
    @Override
    public SortedSet<AgencyYearMonthYTD> collate(Iterable<Map.Entry<AgencyYearPair, Double[]>> values){

        SortedSet<AgencyYearMonthYTD> set = new TreeSet<>(
                (object1, object2) -> {
                    int c;
                    if((c = object1.getAgency().compareTo(object2.getAgency())) != 0){
                        return c;
                    } else if ((c = object1.getYear() - object2.getYear()) != 0){
                        return c;
                    } else {
                        return object1.getMonth() - object2.getMonth();
                    }
                }
        );

        for(Map.Entry<AgencyYearPair, Double[]> entry : values){
            for(int i = 0; i < entry.getValue().length; i++){
                set.add(new AgencyYearMonthYTD(
                        entry.getKey().getAgency(),
                        entry.getKey().getYear(),
                        i + 1,
                        entry.getValue()[i]
                        ));
            }
        }

        return set;
    }
}

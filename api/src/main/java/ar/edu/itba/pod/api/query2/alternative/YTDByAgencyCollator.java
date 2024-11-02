package ar.edu.itba.pod.api.query2.alternative;

import ar.edu.itba.pod.api.model.dto.AgencyYear;
import ar.edu.itba.pod.api.model.dto.AgencyYearMonthYTD;
import com.hazelcast.mapreduce.Collator;


import java.util.Arrays;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class YTDByAgencyCollator implements Collator<Map.Entry<AgencyYear, Integer[]>, SortedSet<AgencyYearMonthYTD>> {
    @Override
    public SortedSet<AgencyYearMonthYTD> collate(Iterable<Map.Entry<AgencyYear, Integer[]>> values) {

        SortedSet<AgencyYearMonthYTD> set = new TreeSet<>(
                (agencyYTD1, agencyYTD2) -> {
                    int c;
                    if ((c = agencyYTD1.getAgency().compareTo(agencyYTD2.getAgency())) != 0){
                        return c;
                    } else if ((c = agencyYTD1.getYear() - agencyYTD2.getYear()) != 0){
                        return c;
                    } else {
                        return agencyYTD1.getMonth() - agencyYTD2.getMonth();
                    }
                }
        );

        for(Map.Entry<AgencyYear, Integer[]> entry : values){
            if (!Arrays.stream(entry.getValue()).allMatch(e -> e==0)){
                int ytd = 0;
                for(int i = 0; i < entry.getValue().length; i++){
                    if (entry.getValue()[i] != 0){
                        ytd += entry.getValue()[i];

                        set.add(new AgencyYearMonthYTD(
                                entry.getKey().getAgency(),
                                entry.getKey().getYear(),
                                i + 1,
                                ytd
                        ));
                    }

                }
            }
        }

        return set;
    }
}

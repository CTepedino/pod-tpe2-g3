package ar.edu.itba.pod.api.query3.percent;

import ar.edu.itba.pod.api.model.dto.CountyPercent;
import ar.edu.itba.pod.api.model.dto.PlateCounter;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentCollator implements Collator<Map.Entry<String, PlateCounter>, SortedSet<CountyPercent>> {
    @Override
    public SortedSet<CountyPercent> collate(Iterable<Map.Entry<String, PlateCounter>> values) {
        SortedSet<CountyPercent> set = new TreeSet<>(
                (county1, county2) -> {
                    int c;
                    if ((c = Double.compare(county2.getPercent(), county1.getPercent())) != 0){
                        return c;
                    }
                    return county1.getCounty().compareTo(county2.getCounty());
                }
        );

        for (Map.Entry<String, PlateCounter> entry : values){
            set.add(new CountyPercent(entry.getKey(), (double) (entry.getValue().getRepeaters()* 100)/entry.getValue().getTotal()));
        }

        return set;
    }
}

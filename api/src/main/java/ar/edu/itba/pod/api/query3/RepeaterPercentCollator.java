package ar.edu.itba.pod.api.query3;

import ar.edu.itba.pod.api.model.dto.CountyPercent;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class RepeaterPercentCollator implements Collator<Map.Entry<String, Double>, SortedSet<CountyPercent>> {
    @Override
    public SortedSet<CountyPercent> collate(Iterable<Map.Entry<String, Double>> values) {
        SortedSet<CountyPercent> set = new TreeSet<>(
                (percent1, percent2) -> {
                    int c;
                    if ((c = Double.compare(percent2.getPercent(), percent1.getPercent())) != 0){
                        return c;
                    }
                    return percent1.getCounty().compareTo(percent2.getCounty());
                }
        );

        for (Map.Entry<String, Double> entry : values){
            set.add(new CountyPercent(entry.getKey(), entry.getValue()));
        }

        return set;
    }
}

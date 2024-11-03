package ar.edu.itba.pod.api.query3;

import ar.edu.itba.pod.api.model.dto.CountyPercent;
import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("deprecation")
public class RepeatOffendersPercentCollator implements Collator<Map.Entry<String, Map<String, Long>>, SortedSet<CountyPercent>> {
    private final int minRepeats;

    public RepeatOffendersPercentCollator(int minRepeats){
        this.minRepeats = minRepeats;
    }

    @Override
    public SortedSet<CountyPercent> collate(Iterable<Map.Entry<String, Map<String, Long>>> values) {
        SortedSet<CountyPercent> set = new TreeSet<>(
                (county1, county2) -> {
                    int c;
                    if ((c = Double.compare(county2.getPercent(), county1.getPercent())) != 0){
                        return c;
                    }
                    return county1.getCounty().compareTo(county2.getCounty());
                }
        );

        for (Map.Entry<String, Map<String, Long>> entry : values){
            int total = entry.getValue().size();
            int repeated = 0;
            for (Map.Entry<String, Long> plate : entry.getValue().entrySet()){
                if (plate.getValue() >= minRepeats){
                    repeated++;
                }
            }

            set.add(new CountyPercent(entry.getKey(), (double) (repeated * 100) /total));
        }

        return set;
    }
}

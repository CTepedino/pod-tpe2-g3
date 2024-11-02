package ar.edu.itba.pod.api.query4;

import ar.edu.itba.pod.api.model.dto.InfractionRange;
import ar.edu.itba.pod.api.model.Range;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class TopNInfractionsByFineRangeCollator implements Collator<Map.Entry<String, Range>, List<InfractionRange>> {
    private final IMap<String, String> infractions;
    private final int topN;

    public TopNInfractionsByFineRangeCollator(IMap<String, String> infractions, int topN){
        this.infractions = infractions;
        this.topN = topN;
    }

    @Override
    public List<InfractionRange> collate(Iterable<Map.Entry<String, Range>> values) {

        SortedSet<InfractionRange> set = new TreeSet<>(
                (range1, range2) -> {
                    int c;
                    if ((c = Double.compare(range2.getRange().getDifference(), range1.getRange().getDifference())) != 0){
                        return c;
                    }
                    return range1.getInfractionDescription().compareTo(range2.getInfractionDescription());
                }
        );

        for (Map.Entry<String, Range> value : values){
            set.add(new InfractionRange(infractions.get(value.getKey()), value.getValue()));
        }

        return set.stream().limit(topN).collect(Collectors.toList());
    }
}

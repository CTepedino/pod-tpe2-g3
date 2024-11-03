package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionRepeaters implements DataSerializable {
    private long totalPlates;
    private long repeaterPlates;

    public InfractionRepeaters(){}

    public InfractionRepeaters(long totalPlates, long repeaterPlates) {
        this.totalPlates = totalPlates;
        this.repeaterPlates = repeaterPlates;
    }

    public long getTotalPlates() {
        return totalPlates;
    }

    public void incrementTotal(long increment){
        totalPlates += increment;
    }

    public long getRepeaterPlates() {
        return repeaterPlates;
    }

    public void incrementRepeaters(long increment){
        repeaterPlates += increment;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(totalPlates);
        out.writeLong(repeaterPlates);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        totalPlates = in.readLong();
        repeaterPlates = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionRepeaters that)) return false;
        return totalPlates == that.totalPlates && repeaterPlates == that.repeaterPlates;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPlates, repeaterPlates);
    }
}

package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class PlateCounter implements DataSerializable {
    private long total;
    private long repeaters;

    public PlateCounter(){}

    public PlateCounter(long total, long repeaters) {
        this.total = total;
        this.repeaters = repeaters;
    }

    public long getTotal() {
        return total;
    }

    public long getRepeaters() {
        return repeaters;
    }

    public void increaseTotal(long n){
        total += n;
    }

    public void increaseRepeaters(long n){
        repeaters += n;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(total);
        out.writeLong(repeaters);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        total = in.readLong();
        repeaters = in.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlateCounter that)) return false;
        return total == that.total && repeaters == that.repeaters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, repeaters);
    }
}

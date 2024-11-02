package ar.edu.itba.pod.api.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class Range implements DataSerializable {
    private int valueMin;
    private int valueMax;

    public Range(){}

    public Range(int valueMin, int valueMax) {
        this.valueMin = valueMin;
        this.valueMax = valueMax;
    }

    public int getValueMin() {
        return valueMin;
    }

    public int getValueMax() {
        return valueMax;
    }

    public void setValueMin(int valueMin) {
        this.valueMin = valueMin;
    }

    public void setValueMax(int valueMax) {
        this.valueMax = valueMax;
    }

    public int getDifference(){
        return valueMax - valueMin;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(valueMin);
        out.writeInt(valueMax);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        valueMin = in.readInt();
        valueMax = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range range)) return false;
        return valueMin == range.valueMin && valueMax == range.valueMax;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueMin, valueMax);
    }
}

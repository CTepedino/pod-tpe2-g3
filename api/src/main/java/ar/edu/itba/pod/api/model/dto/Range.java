package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class Range implements DataSerializable {
    private double valueMin;
    private double valueMax;

    public Range(){}

    public Range(double valueMin, double valueMax) {
        this.valueMin = valueMin;
        this.valueMax = valueMax;
    }

    public double getValueMin() {
        return valueMin;
    }

    public double getValueMax() {
        return valueMax;
    }

    public double getRange(){
        return valueMax - valueMin;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(valueMin);
        out.writeDouble(valueMax);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        valueMin = in.readDouble();
        valueMax = in.readDouble();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range range)) return false;
        return Double.compare(valueMin, range.valueMin) == 0 && Double.compare(valueMax, range.valueMax) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueMin, valueMax);
    }

}

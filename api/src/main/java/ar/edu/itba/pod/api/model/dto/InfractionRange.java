package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionRange implements DataSerializable {
    private String infractionDescription;
    private Range range;

    public InfractionRange(){}

    public InfractionRange(String infractionDescription, Range range) {
        this.infractionDescription = infractionDescription;
        this.range = range;
    }

    public String getInfractionDescription() {
        return infractionDescription;
    }

    public Range getRange() {
        return range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionRange that)) return false;
        return Objects.equals(infractionDescription, that.infractionDescription) && Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infractionDescription, range);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(infractionDescription);
        out.writeObject(range);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        infractionDescription = in.readUTF();
        range = in.readObject();
    }
}

package ar.edu.itba.pod.api.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Infraction implements DataSerializable {
    private String id;
    private String definition;

    public Infraction(){}

    public Infraction(String id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(definition);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readUTF();
        definition = in.readUTF();
    }
}

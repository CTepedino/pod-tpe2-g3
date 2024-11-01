package ar.edu.itba.pod.api.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Agency implements DataSerializable {
    private String issuingAgency;

    public Agency(){}

    public Agency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        issuingAgency = in.readUTF();
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }
}


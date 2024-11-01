package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionAgencyPair implements DataSerializable {
    private String agency;
    private String infractionId;

    public InfractionAgencyPair(){}

    public InfractionAgencyPair(String agency, String infractionId) {
        this.agency = agency;
        this.infractionId = infractionId;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agency);
        out.writeUTF(infractionId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        infractionId = in.readUTF();
    }

    public String getAgency() {
        return agency;
    }

    public String getInfractionId() {
        return infractionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionAgencyPair that)) return false;
        return Objects.equals(agency, that.agency) && Objects.equals(infractionId, that.infractionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, infractionId);
    }
}

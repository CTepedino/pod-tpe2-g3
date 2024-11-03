package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionAgency implements DataSerializable {
    private String agency = "";
    private String infractionId = "";

    public InfractionAgency(){}

    public InfractionAgency(String agency, String infractionId) {
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
        if (!(o instanceof InfractionAgency that)) return false;
        return Objects.equals(agency, that.agency) && Objects.equals(infractionId, that.infractionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, infractionId);
    }
}

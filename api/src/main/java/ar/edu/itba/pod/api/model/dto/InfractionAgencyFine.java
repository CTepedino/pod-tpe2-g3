package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionAgencyFine implements DataSerializable {
    private String infractionId;
    private String agency;
    private double fine;

    public InfractionAgencyFine(){}

    public InfractionAgencyFine(String infractionId, String agency, double fine) {
        this.infractionId = infractionId;
        this.agency = agency;
        this.fine = fine;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(infractionId);
        out.writeUTF(agency);
        out.writeDouble(fine);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        infractionId = in.readUTF();
        agency = in.readUTF();
        fine = in.readDouble();
    }

    public String getInfractionId() {
        return infractionId;
    }

    public String getAgency() {
        return agency;
    }

    public double getFine() {
        return fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionAgencyFine that)) return false;
        return Double.compare(fine, that.fine) == 0 && Objects.equals(infractionId, that.infractionId) && Objects.equals(agency, that.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infractionId, agency, fine);
    }
}

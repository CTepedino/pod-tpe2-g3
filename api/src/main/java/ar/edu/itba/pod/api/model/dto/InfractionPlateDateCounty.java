package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class InfractionPlateDateCounty implements DataSerializable {
    private String plate;
    private LocalDate issueDate;
    private String county;
    private String infraction;

    public InfractionPlateDateCounty(){}

    public InfractionPlateDateCounty(String plate, LocalDate issueDate, String county, String infraction) {
        this.plate = plate;
        this.issueDate = issueDate;
        this.county = county;
        this.infraction = infraction;
    }

    public String getPlate() {
        return plate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getCounty() {
        return county;
    }

    public String getInfraction() {
        return infraction;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeObject(issueDate);
        out.writeUTF(county);
        out.writeUTF(infraction);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        issueDate = in.readObject();
        county = in.readUTF();
        infraction = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionPlateDateCounty that)) return false;
        return Objects.equals(plate, that.plate) && Objects.equals(issueDate, that.issueDate) && Objects.equals(county, that.county) && Objects.equals(infraction, that.infraction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, issueDate, county, infraction);
    }
}

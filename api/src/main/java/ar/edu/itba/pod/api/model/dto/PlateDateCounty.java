package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class PlateDateCounty implements DataSerializable {
    private String plate;
    private LocalDate issueDate;
    private String county;

    public PlateDateCounty(){}

    public PlateDateCounty(String plate, LocalDate issueDate, String county) {
        this.plate = plate;
        this.issueDate = issueDate;
        this.county = county;
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

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeObject(issueDate);
        out.writeUTF(county);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        issueDate = in.readObject();
        county = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlateDateCounty that)) return false;
        return Objects.equals(plate, that.plate) && Objects.equals(issueDate, that.issueDate) && Objects.equals(county, that.county);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, issueDate, county);
    }
}

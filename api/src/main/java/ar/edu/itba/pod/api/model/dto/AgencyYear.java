package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class AgencyYear implements DataSerializable {
    private String agency;
    private int year;

    public AgencyYear() {}

    public AgencyYear(String agency, int year) {
        this.agency = agency;
        this.year = year;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agency);
        out.writeInt(year);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        year = in.readInt();
    }

    public String getAgency() {
        return agency;
    }

    public int getYear(){
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyYear that)) return false;
        return agency.equals(that.agency) && year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, year);
    }
}
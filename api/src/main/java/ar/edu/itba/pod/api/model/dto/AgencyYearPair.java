package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class AgencyYearPair implements DataSerializable {
    private String agency;
    private int year;

    public AgencyYearPair() {}

    public AgencyYearPair(String agency, int year) {
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
        if (!(o instanceof AgencyYearPair that)) return false;
        return Objects.equals(agency, that.agency) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, year);
    }
}
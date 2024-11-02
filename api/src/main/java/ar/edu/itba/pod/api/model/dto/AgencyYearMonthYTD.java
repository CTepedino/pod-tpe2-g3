package ar.edu.itba.pod.api.model.dto;

import ar.edu.itba.pod.api.model.CSVPrintable;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class AgencyYearMonthYTD implements DataSerializable, CSVPrintable {
    private String agency;
    private int year;
    private int month;
    private Double ytd;

    public AgencyYearMonthYTD() {}

    public AgencyYearMonthYTD(String agency, int year, int month, Double ytd) {
        this.agency = agency;
        this.year = year;
        this.month = month;
        this.ytd = ytd;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agency);
        out.writeInt(year);
        out.writeInt(month);
        out.writeDouble(ytd);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        year = in.readInt();
        month = in.readInt();
        ytd = in.readDouble();
    }

    public String getAgency() {
        return agency;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Double getYtd() {
        return ytd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyYearMonthYTD that)) return false;
        return Objects.equals(agency, that.agency) && year == that.year && month == that.month && Objects.equals(ytd, that.ytd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, year, month, ytd);
    }

    @Override
    public String printAsCSV(char separator){
        return agency + separator + year + separator + month + separator + (int) ytd.doubleValue();
    }
}

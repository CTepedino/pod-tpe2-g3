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
    private int ytd;

    public AgencyYearMonthYTD() {}

    public AgencyYearMonthYTD(String agency, int year, int month, int ytd) {
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
        out.writeInt(ytd);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        year = in.readInt();
        month = in.readInt();
        ytd = in.readInt();
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

    public int getYtd() {
        return ytd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyYearMonthYTD that)) return false;
        return agency.equals(that.agency) && year == that.year && month == that.month && ytd == that.ytd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, year, month, ytd);
    }

    @Override
    public String printAsCSV(char separator){
        return agency + separator + year + separator + month + separator + ytd;
    }
}

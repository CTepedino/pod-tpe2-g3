package ar.edu.itba.pod.api.model.dto;

import ar.edu.itba.pod.api.model.CSVPrintable;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public class CountyPercent implements DataSerializable, CSVPrintable {
    private String county;
    private double percent;

    public CountyPercent(){}

    public CountyPercent(String county, double percent) {
        this.county = county;
        this.percent = percent;
    }

    public String getCounty() {
        return county;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(county);
        out.writeDouble(percent);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        county = in.readUTF();
        percent = in.readDouble();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountyPercent that)) return false;
        return Double.compare(percent, that.percent) == 0 && Objects.equals(county, that.county);
    }

    @Override
    public int hashCode() {
        return Objects.hash(county, percent);
    }

    @Override
    public String printAsCSV(char separator) {
        String percentString = BigDecimal.valueOf(percent).setScale(2, RoundingMode.DOWN) + "%";
        return county + separator + percentString;
    }
}

package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Month;
import java.util.Objects;

public class MonthFine implements DataSerializable {
    private Month month;
    private int fine;

    public MonthFine(){}

    public MonthFine(Month month, int fine) {
        this.month = month;
        this.fine = fine;
    }

    public Month getMonth() {
        return month;
    }

    public int getFine() {
        return fine;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(month.getValue());
        out.writeInt(fine);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        month = Month.of(in.readInt());
        fine = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthFine monthFine)) return false;
        return fine == monthFine.fine && month == monthFine.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, fine);
    }
}

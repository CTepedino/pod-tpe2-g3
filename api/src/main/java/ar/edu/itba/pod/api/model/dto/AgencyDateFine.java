package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class AgencyDateFine implements DataSerializable {
    private String agency;
    private LocalDate date;
    private int fine;

    public AgencyDateFine(){}

    public AgencyDateFine(String agency, LocalDate date, int fine) {
        this.agency = agency;
        this.date = date;
        this.fine = fine;
    }

    public String getAgency() {
        return agency;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getFine() {
        return fine;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agency);
        out.writeObject(date);
        out.writeInt(fine);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        date = in.readObject();
        fine = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyDateFine that)) return false;
        return fine == that.fine && Objects.equals(agency, that.agency) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, date, fine);
    }
}

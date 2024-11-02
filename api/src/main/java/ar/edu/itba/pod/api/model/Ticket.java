package ar.edu.itba.pod.api.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDate;

public class Ticket implements DataSerializable {

    private String plate;
    private String infractionId;
    private int fineAmount;
    private String issuingAgency;
    private LocalDate issueDate;
    private String countyName;

    public Ticket() {}

    public Ticket(String plate, String infractionId, int fineAmount, String issuingAgency, LocalDate issueDate, String countyName) {
        this.plate = plate;
        this.infractionId = infractionId;
        this.fineAmount = fineAmount;
        this.issuingAgency = issuingAgency;
        this.issueDate = issueDate;
        this.countyName = countyName;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(plate);
        out.writeUTF(infractionId);
        out.writeInt(fineAmount);
        out.writeUTF(issuingAgency);
        out.writeObject(issueDate);
        out.writeUTF(countyName);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        plate = in.readUTF();
        infractionId = in.readUTF();
        fineAmount = in.readInt();
        issuingAgency = in.readUTF();
        issueDate = in.readObject();
        countyName = in.readUTF();
    }

    public String getPlate() {
        return plate;
    }

    public String getInfractionId() {
        return infractionId;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getCountyName() {
        return countyName;
    }
}

package ar.edu.itba.pod.api.model.dto;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class InfractionAgencyTicketCount implements DataSerializable {
    private String infraction;
    private String agency;
    private long ticketCount;

    public InfractionAgencyTicketCount() {}

    public InfractionAgencyTicketCount(String infraction, String agency, long ticketCount) {
        this.infraction = infraction;
        this.agency = agency;
        this.ticketCount = ticketCount;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(infraction);
        out.writeUTF(agency);
        out.writeLong(ticketCount);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        infraction = in.readUTF();
        agency = in.readUTF();
        ticketCount = in.readLong();
    }

    public String getInfraction() {
        return infraction;
    }

    public String getAgency() {
        return agency;
    }

    public long getTicketCount() {
        return ticketCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InfractionAgencyTicketCount that)) return false;
        return ticketCount == that.ticketCount && Objects.equals(infraction, that.infraction) && Objects.equals(agency, that.agency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infraction, agency, ticketCount);
    }

}

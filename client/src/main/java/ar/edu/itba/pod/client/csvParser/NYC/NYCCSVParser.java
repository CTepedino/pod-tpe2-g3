package ar.edu.itba.pod.client.csvParser.NYC;

import ar.edu.itba.pod.client.csvParser.CSVFileParser;
import ar.edu.itba.pod.client.csvParser.CityCSVParser;
import ar.edu.itba.pod.api.model.Agency;
import ar.edu.itba.pod.api.model.Infraction;
import ar.edu.itba.pod.api.model.Ticket;

public class NYCCSVParser extends CityCSVParser {
    public NYCCSVParser(String path){
        super(path, "NYC");
    }

    @Override
    public CSVFileParser<Ticket> getTicketFileParser() {
        return super.getTicketFileParser(true, new NYCTicketLineParser());
    }

    @Override
    public CSVFileParser<Infraction> getInfractionFileParser() {
        return super.getInfractionFileParser(true, new NYCInfractionLineParser());
    }

    @Override
    public CSVFileParser<Agency> getAgencyFileParser() {
        return super.getAgencyFileParser(true, new NYCAgencyLineParser());
    }
}

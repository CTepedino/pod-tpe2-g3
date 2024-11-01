package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVFileParser;
import ar.edu.itba.pod.client.csvParser.CityCSVParser;
import ar.edu.itba.pod.api.model.Agency;
import ar.edu.itba.pod.api.model.Infraction;
import ar.edu.itba.pod.api.model.Ticket;
import ar.edu.itba.pod.client.query.City;

public class CHICSVParser extends CityCSVParser {
    public CHICSVParser(String path){
        super(path, "CHI");
    }

    @Override
    public CSVFileParser<Ticket> getTicketFileParser() {
        return super.getTicketFileParser(true, new CHITicketLineParser());
    }

    @Override
    public CSVFileParser<Infraction> getInfractionFileParser() {
        return super.getInfractionFileParser(true, new CHIInfractionLineParser());
    }

    @Override
    public CSVFileParser<Agency> getAgencyFileParser() {
        return super.getAgencyFileParser(true, new CHIAgencyLineParser());
    }
}

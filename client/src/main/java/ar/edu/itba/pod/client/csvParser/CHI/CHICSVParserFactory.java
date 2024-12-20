package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVFileParser;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import ar.edu.itba.pod.api.model.Infraction;
import ar.edu.itba.pod.api.model.Ticket;

public class CHICSVParserFactory extends CityCSVParserFactory {
    public CHICSVParserFactory(String path){
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
    public CSVFileParser<String> getAgencyFileParser() {
        return super.getAgencyFileParser(true, new CHIAgencyLineParser());
    }
}

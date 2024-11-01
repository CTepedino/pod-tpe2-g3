package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Agency;

public class CHIAgencyLineParser implements CSVLineParser<Agency> {

    @Override
    public Agency parseLine(String line) {
        return new Agency(line);
    }
}

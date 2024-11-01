package ar.edu.itba.pod.client.csvParser.NYC;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Agency;

public class NYCAgencyLineParser implements CSVLineParser<Agency> {

    @Override
    public Agency parseLine(String line){
        return new Agency(line);
    }
}

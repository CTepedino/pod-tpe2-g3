package ar.edu.itba.pod.client.csvParser.NYC;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;

import java.util.function.Function;

public class NYCAgencyLineParser extends CSVLineParser<String> {

    @Override
    public String parseLine(String line){
        return line;
    }
}

package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;

import java.util.function.Function;

public class CHIAgencyLineParser extends CSVLineParser<String> {

    @Override
    public String parseLine(String line) {
        return line;
    }
}

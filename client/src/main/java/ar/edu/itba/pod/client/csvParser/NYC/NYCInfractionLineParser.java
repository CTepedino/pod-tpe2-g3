package ar.edu.itba.pod.client.csvParser.NYC;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Infraction;

import java.util.StringTokenizer;

public class NYCInfractionLineParser extends CSVLineParser<Infraction> {
    private static final String SEPARATOR = ";";


    @Override
    public Infraction parseLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);
        String id = tokenizer.nextToken();
        String definition = tokenizer.nextToken();
        return new Infraction(id, definition);
    }
}

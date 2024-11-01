package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Infraction;

import java.util.StringTokenizer;
import java.util.function.Function;

public class CHIInfractionLineParser extends CSVLineParser<Infraction> {
    private static final String SEPARATOR = ";";


    @Override
    public Infraction parseLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);
        String code = tokenizer.nextToken();
        String description = tokenizer.nextToken();
        return new Infraction(code, description);
    }

}

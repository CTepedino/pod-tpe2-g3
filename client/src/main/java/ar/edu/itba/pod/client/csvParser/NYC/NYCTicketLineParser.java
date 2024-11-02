package ar.edu.itba.pod.client.csvParser.NYC;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Ticket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class NYCTicketLineParser extends CSVLineParser<Ticket> {
    private static final String SEPARATOR = ";";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public Ticket parseLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);
        String plate = tokenizer.nextToken();
        String id = tokenizer.nextToken();
        int amount = (int) Double.parseDouble(tokenizer.nextToken());
        String agency = tokenizer.nextToken();
        LocalDate date = LocalDate.parse(tokenizer.nextToken(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        String county = tokenizer.nextToken();

        return new Ticket(plate, id, amount, agency, date, county);
    }
}

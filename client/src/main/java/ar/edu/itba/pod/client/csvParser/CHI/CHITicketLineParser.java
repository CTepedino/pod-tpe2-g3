package ar.edu.itba.pod.client.csvParser.CHI;

import ar.edu.itba.pod.client.csvParser.CSVLineParser;
import ar.edu.itba.pod.api.model.Ticket;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class CHITicketLineParser extends CSVLineParser<Ticket> {
    private static final String SEPARATOR = ";";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Ticket parseLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);
        LocalDate date = LocalDate.parse(tokenizer.nextToken(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        String community = tokenizer.nextToken();
        String agency = tokenizer.nextToken();
        String plate = tokenizer.nextToken();
        String code = tokenizer.nextToken();
        int amount = Integer.parseInt(tokenizer.nextToken());

        return new Ticket(plate, code, amount, agency, date, community);
    }
}

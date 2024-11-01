package ar.edu.itba.pod.client.csvParser;

import ar.edu.itba.pod.api.model.Agency;
import ar.edu.itba.pod.api.model.Infraction;
import ar.edu.itba.pod.api.model.Ticket;

public abstract class CityCSVParserFactory {

    private static final String TICKETS_CSV_FILENAME = "tickets";
    private static final String INFRACTION_CSV_FILENAME = "infractions";
    private static final String AGENCIES_CSV_FILENAME = "agencies";
    private static final String FILETYPE_EXTENSION = ".csv";

    private final String path;
    private final String cityCode;

    protected CityCSVParserFactory(String path, String cityCode){
        this.path = path;
        this.cityCode = cityCode;
    }

    private String buildFilePath(String name){
        return path + '/' + name + cityCode + FILETYPE_EXTENSION;
    }

    private <T> CSVFileParser<T> getFileParser(String fileName, boolean hasHeader, CSVLineParser<T> lineParser){
        return new CSVFileParser<>(buildFilePath(fileName), hasHeader, lineParser);
    }

    protected CSVFileParser<Ticket> getTicketFileParser(boolean hasHeader, CSVLineParser<Ticket> lineParser){
        return getFileParser(TICKETS_CSV_FILENAME, hasHeader, lineParser);
    }
    public abstract CSVFileParser<Ticket> getTicketFileParser();

    protected CSVFileParser<Infraction> getInfractionFileParser(boolean hasHeader, CSVLineParser<Infraction> lineParser){
        return getFileParser(INFRACTION_CSV_FILENAME, hasHeader, lineParser);
    }
    public abstract CSVFileParser<Infraction> getInfractionFileParser();

    protected CSVFileParser<Agency> getAgencyFileParser(boolean hasHeader, CSVLineParser<Agency> lineParser){
        return getFileParser(AGENCIES_CSV_FILENAME, hasHeader, lineParser);
    }
    public abstract CSVFileParser<Agency> getAgencyFileParser();

}

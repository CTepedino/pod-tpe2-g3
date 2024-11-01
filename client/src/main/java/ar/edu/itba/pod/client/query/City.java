package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.client.csvParser.CHI.CHICSVParserFactory;
import ar.edu.itba.pod.client.csvParser.CityCSVParserFactory;
import ar.edu.itba.pod.client.csvParser.NYC.NYCCSVParserFactory;

public enum City {
    NEW_YORK("NYC"){
        @Override
        public CityCSVParserFactory getParser(String path){
            return new NYCCSVParserFactory(path);
        }
    },
    CHICAGO("CHI"){
        @Override
        public CityCSVParserFactory getParser(String path) {
            return new CHICSVParserFactory(path);
        }
    };

    private final String cityCode;

    City(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public static City fromCode(String code) {
        for (City city : City.values()) {
            if (city.cityCode.equalsIgnoreCase(code)) {
                return city;
            }
        }
        return null;
    }

    public abstract CityCSVParserFactory getParser(String path);

}

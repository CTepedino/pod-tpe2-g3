package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.client.csvParser.CHI.CHICSVParser;
import ar.edu.itba.pod.client.csvParser.CityCSVParser;
import ar.edu.itba.pod.client.csvParser.NYC.NYCCSVParser;

public enum City {
    NEW_YORK("NYC"){
        @Override
        public CityCSVParser getParser(String path){
            return new NYCCSVParser(path);
        }
    },
    CHICAGO("CHI"){
        @Override
        public CityCSVParser getParser(String path) {
            return new CHICSVParser(path);
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

    public abstract CityCSVParser getParser(String path);

}

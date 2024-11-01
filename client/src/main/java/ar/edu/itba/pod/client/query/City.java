package ar.edu.itba.pod.client.query;

public enum City {
    NEW_YORK("NYC"),
    CHICAGO("CHI");

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


}
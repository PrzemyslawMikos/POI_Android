package additional;

import constants.TextsConstants;

/**
 * Created by Przemek on 29.12.2016.
 */

public class GeoAddress implements TextsConstants{

    private String address, city, state, country, postalcode, knownName;

    public GeoAddress(String address, String city, String state, String country, String postalcode, String knownName) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalcode = postalcode;
        this.knownName = knownName;
    }

    public String getCompleteAddress(String noData){
        return String.format(TEXT_GOOGLE_LOCATION,
                address != null ? address : noData,
                city != null ? city : noData,
                state != null ? state : noData,
                country != null ? country : noData,
                postalcode != null ? postalcode : noData,
                knownName != null ? knownName : noData
        );
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getKnownName() {
        return knownName;
    }
}
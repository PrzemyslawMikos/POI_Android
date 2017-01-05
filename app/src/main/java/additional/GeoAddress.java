package additional;

import android.util.Log;

import java.lang.reflect.Field;

import constants.TextsConstants;

/**
 * Created by Przemek on 29.12.2016.
 */

public class GeoAddress implements TextsConstants{

    //TODO zabezpieczenia jeśli coś nie zostanie ustawione!!

    private String address, city, state, country, postalcode, knownName;

    public GeoAddress(String address, String city, String state, String country, String postalcode, String knownName) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalcode = postalcode;
        this.knownName = knownName;
    }

    public String getCompleteAddress(){

        StringBuilder stringBuilder = new StringBuilder();

        for (Field f : getClass().getFields()) {
            f.setAccessible(true);
            try {
                if (f.get(this) != null) {
                    Log.d("nope", "nope");
                    stringBuilder.append(f.get(this));
                }
            } catch (IllegalAccessException e) { // shouldn't happen because I used setAccessible
            }
        }
        try{
            return String.format(TEXT_GOOGLE_LOCATION, address, city, state, country, postalcode, knownName);
        }
        catch(Exception e){
            return "bang";
        }
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
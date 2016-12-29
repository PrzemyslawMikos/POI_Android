package additional;

import android.content.ContentValues;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;
import java.util.Locale;

import entity.PointEntity;

/**
 * Created by Przemek on 29.12.2016.
 */

public class GoogleLocation {

    PointEntity point;
    GeoAddress geoAddress;
    Context context;


    public GoogleLocation(PointEntity point, Context context){
        this.point = point;
        this.context = context;
        setAddress();
    }

    public GeoAddress getAddress(){
        return this.geoAddress;
    }

    private void setAddress() {
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
            geoAddress = new GeoAddress(addresses.get(0).getAddressLine(0), addresses.get(0).getLocality(), addresses.get(0).getAdminArea(), addresses.get(0).getCountryName(), addresses.get(0).getPostalCode(), addresses.get(0).getFeatureName());
        }
        catch (Exception e){
        }
    }

}
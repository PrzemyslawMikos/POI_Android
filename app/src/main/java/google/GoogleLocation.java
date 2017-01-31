package google;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import java.util.List;
import java.util.Locale;
import additional.GeoAddress;
import delegates.GoogleLocationTaskDelegate;
/**
 * Created by Przemysław Mikos on 29.12.2016.
 */

public class GoogleLocation extends AsyncTask<Location, GeoAddress, GeoAddress> {

    private Context context;
    private GoogleLocationTaskDelegate delegate;

    public GoogleLocation(Context context, GoogleLocationTaskDelegate delegate){
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected GeoAddress doInBackground(Location... params) {
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(params[0].getLatitude(), params[0].getLongitude(), 1);
            GeoAddress geoAddress = new GeoAddress(addresses.get(0).getAddressLine(0), addresses.get(0).getLocality(), addresses.get(0).getAdminArea(), addresses.get(0).getCountryName(), addresses.get(0).getPostalCode(), addresses.get(0).getFeatureName());
            return geoAddress;
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(GeoAddress address) {
        delegate.TaskCompletionResult(address);
    }
}
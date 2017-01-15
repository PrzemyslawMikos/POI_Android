package additional;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import delegates.LocationDelegate;

/**
 * Created by Przemek on 27.12.2016.
 */

public class SingleLocationHelper implements LocationListener {

    private LocationManager locationManager;
    private Location currentLocation;
    private LocationDelegate locationDelegate;
    private ProgressDialog dialog;

    public SingleLocationHelper(Context context, String dialogMessage, LocationDelegate locationDelegate) {
        this.locationDelegate = locationDelegate;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            this.dialog = new ProgressDialog(context);
            this.dialog.setMessage(dialogMessage);
            this.dialog.show();
        }
    }

    public void stopLocationManager(Context context) {
        // TODO sprawdzanie
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        locationDelegate.TaskCompletionResult(location);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
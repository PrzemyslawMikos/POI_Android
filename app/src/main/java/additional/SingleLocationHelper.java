package additional;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import delegates.LocationDelegate;

/**
 * Created by Przemek on 27.12.2016.
 */

public class SingleLocationHelper implements LocationListener {

    private LocationManager locationManager;
    private LocationDelegate locationDelegate;
    private ProgressDialog dialog;

    public SingleLocationHelper(Activity activity, String dialogMessage, LocationDelegate locationDelegate) {
        this.locationDelegate = locationDelegate;
        locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (PermissionHelper.checkLocationPermission(activity.getApplicationContext())) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            this.dialog = new ProgressDialog(activity);
            this.dialog.setMessage(dialogMessage);
            this.dialog.show();
        }
    }

    public void stopLocationManager(Context context) {
        if (PermissionHelper.checkLocationPermission(context)) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
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
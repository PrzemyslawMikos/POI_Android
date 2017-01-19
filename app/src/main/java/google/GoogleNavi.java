package google;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import constants.MainConstants;
import entity.PointEntity;

/**
 * Created by Przemysław Mikos on 29.12.2016.
 */

public final class GoogleNavi implements MainConstants {

    private static GoogleNavi googleNavi;

    public static void startNavi(String mode, Activity activity, PointEntity point){
        Uri gmmIntentUri = Uri.parse(String.format(mode, point.getLatitude(), point.getLongitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(GOOGLE_NAVI_PACKAGE);
        activity.startActivity(mapIntent);
    }
}
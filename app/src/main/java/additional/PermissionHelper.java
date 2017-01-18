package additional;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Przemek on 17.01.2017.
 */

public final class PermissionHelper {

    private static PermissionHelper permissionHelper;

    public static boolean checkWriteExternalStoragePermission(Context context){
            String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
            int res = context.checkCallingOrSelfPermission(permission);
            return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkCameraPermission(Context context){
        String permission = "android.permission.CAMERA";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkLocationPermission(Context context){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static Intent getApplicationPermissionsSettings(Context context){
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        return intent;
    }
}
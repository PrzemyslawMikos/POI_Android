package additional;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.adventure.poi.poi_android.R;

/**
 * Created by Przemysław Mikos on 07.01.2017.
 */

public final class SnackbarManager {

    private static SnackbarManager snackbarManager;

    public static void showSnackbar(Activity activity, String message, int duration){
        View view = activity.findViewById(android.R.id.content);
        Snackbar.make(view, message, duration).show();
    }

    public static void showSnackbarWithSettings(final Activity activity, String message, int duration){
        View view = activity.findViewById(android.R.id.content);
        Snackbar.make(view, message, duration).setAction(activity.getResources().getString(R.string.settings_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivity(intent);
            }
        }).show();
    }

    public static void showSnackbarWithOptionIntent(final Activity activity, String message, String optionText, int duration, final Intent intent){
        View view = activity.findViewById(android.R.id.content);
        Snackbar.make(view, message, duration).setAction(optionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(intent);
            }
        }).show();
    }
}
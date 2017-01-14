package additional;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Przemek on 07.01.2017.
 */

public final class SnackbarManager {

    private static SnackbarManager snackbarManager;

    public static void showSnackbar(Activity activity, String message, int duration){
        View view = activity.findViewById(android.R.id.content);
        Snackbar.make(view, message, duration).show();
    }
}

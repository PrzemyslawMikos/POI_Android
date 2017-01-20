package additional;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Przemysław Mikos on 07.01.2017.
 */

public final class ToastManager {

    private static ToastManager toastManager;

    public static void showToast(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
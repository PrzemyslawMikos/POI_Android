package additional;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Przemek on 07.01.2017.
 */

public final class ToastManager {

    private static ToastManager toastManager;

    public static void showToast(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}

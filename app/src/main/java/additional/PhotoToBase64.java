package additional;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import delegates.PhotoToBase64Delegate;

/**
 * Created by Przemek on 14.01.2017.
 */

public class PhotoToBase64 extends AsyncTask<Bitmap, Void, String> {
//TODO wyświetlić dialogboxa który dzisiaj nie działa xddd

    private Context context;
    private PhotoToBase64Delegate pictureToBase64Delegate;
    private ProgressDialog dialog;
    private String dialogMessage;

    public PhotoToBase64(Context context, String dialogMessage, PhotoToBase64Delegate photoToBase64Delegate){
        this.pictureToBase64Delegate = photoToBase64Delegate;
        this.context = context;
        this.dialogMessage = dialogMessage;
//        this.dialog = new ProgressDialog(context);
//        this.dialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
//        this.dialog.setMessage(dialogMessage);
//        this.dialog.show();
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        Bitmap bitmap = params[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onPostExecute(String result) {
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
        if(result != null){
            pictureToBase64Delegate.TaskCompletionResult(result);
        }
    }

}
package task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by Przemysław Mikos on 08.01.2017.
 */

public class PictureLoader extends AsyncTask <URL, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public PictureLoader(ImageView imageView){
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(URL... params) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
            return bitmap;
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result){
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (result != null) {
                    imageView.setImageBitmap(result);
                }
            }
        }
    }

}
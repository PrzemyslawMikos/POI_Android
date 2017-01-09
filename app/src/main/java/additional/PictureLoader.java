package additional;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by Przemek on 08.01.2017.
 */

public class PictureLoader extends AsyncTask <URL, Void, Bitmap> {

    private ImageView imageView;

    public PictureLoader(ImageView imageView){
        this.imageView = imageView;
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
        if(result != null){
            imageView.setImageBitmap(result);
        }
    }
}
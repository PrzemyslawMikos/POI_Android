package additional;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;

import java.net.URL;

import rest.RestHelper;

/**
 * Created by Przemek on 13.12.2016.
 */

public class ImageManager implements ImageTaskDelegate{
    private ImageTaskDelegate delegate;
    private Boolean result;

    public ImageManager(ImageTaskDelegate delegate) {
        this.delegate = delegate;
    }

    public ImageTaskDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(ImageTaskDelegate delegate) {
        this.delegate = delegate;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void runTask(URL url) {
        ImageTask imageTask = new ImageTask(this);
        imageTask.execute(url);
    }

    @Override
    public void TaskCompletionResult(Bitmap result) {
        delegate.TaskCompletionResult(result);
    }

    private class ImageTask extends AsyncTask<URL, Bitmap, Bitmap> {

        private ImageTaskDelegate delegate;

        public ImageTask(ImageTaskDelegate delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
                result = true;
                return bitmap;
            }
            catch (Exception e){
                result = false;
            }
            result = false;
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap entity) {
            delegate.TaskCompletionResult(entity);
        }
    }
}
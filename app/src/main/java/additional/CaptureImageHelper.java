package additional;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import constants.MainConstants;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Przemysław Mikos on 13.01.2017.
 */

public class CaptureImageHelper implements MainConstants{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Activity activity;
    private File fileOutput = null;
    private Bitmap photo;

    public CaptureImageHelper(Activity activity){
        this.activity = activity;
    }

    public Bitmap getPhoto(){
        return photo;
    }

    public void startTakingPhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        fileOutput = new File(file, IMAGE_TMP_NAME);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileOutput));
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(new FileInputStream(fileOutput), null, options);
                originalBitmap = checkRotation(originalBitmap, fileOutput.getAbsolutePath().toString());
                Bitmap resizedBitmap;
                if(originalBitmap.getWidth() > originalBitmap.getHeight()){
                    resizedBitmap = getResizedBitmap(originalBitmap, IMAGE_RESIZED_WIDTH, IMAGE_RESIZED_HEIGHT);
                }
                else{
                    resizedBitmap = getResizedBitmap(originalBitmap, IMAGE_RESIZED_HEIGHT, IMAGE_RESIZED_WIDTH);
                }
                fileOutput.delete();
                photo = resizedBitmap;
            } catch (FileNotFoundException e) {
                fileOutput.delete();
                activity.finish();
                e.printStackTrace();
            } catch (IOException ignored) {
            }
        }
        else{
            fileOutput.delete();
            activity.finish();
        }
    }

    public Bitmap checkRotation(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(photoPath);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);

            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;

            default:
                return bitmap;
        }
    }

    public static Bitmap rotateImage(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap;
        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }
}
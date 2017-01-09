package com.adventure.poi.poi_android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import additional.LocationHelper;
import delegates.RestTaskDelegate;
import additional.SharedPreferencesManager;
import constants.MainConstants;
import entity.PointEntity;
import rest.PointsHelper;
import rest.TypesHelper;

public class AddActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap photo;
    private EditText editName, editDescription, editLocality;
    private Spinner spinnerTypes;
    private ImageView imagePhoto;
    private Button buttonTakePhoto, buttonSend;
    private LocationHelper locationHelper;
    private TypesHelper th;
    private File output = null;
    private String image = null;
    private Location location = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        assignControls();
        locationHelper = new LocationHelper(this);
        th = new TypesHelper(AddActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {

                fillSpinner(th.getTypesNamesList());
            }
        });
        th.getAllTypes("pobieranie typów");


        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir=
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        output=new File(dir, "CameraContentDemo.jpeg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        i.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                    Log.d("nope", "adsd");
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(output), null, options);
                    bang vv = new bang();
                    vv.execute(bitmap);
                imagePhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                output.delete();
                this.finish();
            }
        }
        else{
            output.delete();
            this.finish();
        }
    }
    private void fillSpinner(List<String> listTypes){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypes.setAdapter(dataAdapter);
        spinnerTypes.setSelection(0);
    }

    private void assignControls(){
        editName = (EditText) findViewById(R.id.editTextName);
        editDescription = (EditText) findViewById(R.id.editTextDescription);
        editLocality = (EditText) findViewById(R.id.editTextLocality);
        spinnerTypes = (Spinner) findViewById(R.id.spinnerTypes);
        imagePhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        buttonSend = (Button) findViewById(R.id.buttonSend);
    }

    public void onPhotoClick(View v){

    }

    public void onSendClick(View v){
        location = locationHelper.getCurrentLocation();
        if(image != null && location != null && output != null){
            PointsHelper pointsHelper = new PointsHelper(AddActivity.this, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    Toast t = Toast.makeText(AddActivity.this, "Punkt dodany", Toast.LENGTH_LONG);
                    t.show();
                }
            });
            //TODO poczekaj na pobranie lokalizacji !!

            SharedPreferencesManager prefManager = new SharedPreferencesManager(AddActivity.this);
            image.replaceAll("[\n\r]", "");
            PointEntity pe = new PointEntity(location.getLongitude(), location.getLatitude(), editName.getText().toString(), editLocality.getText().toString(), editDescription.getText().toString(), image, "jpeg", 1, Long.valueOf(prefManager.getPreferenceString(MainConstants.PREFERENCE_USERID)));
            pointsHelper.postPoint("Dodawanie punktu", pe);
            output.delete();
        }
        else{
            Toast t = Toast.makeText(this, "jeszcze dodaje", Toast.LENGTH_LONG);
            t.show();
        }

    }

    private String bitmapToString(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class bang extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap myBitmap = params[0];
            final int maxSize = 50;
            int outWidth;
            int outHeight;
            int inWidth = myBitmap.getWidth();
            int inHeight = myBitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(myBitmap, outWidth, outHeight, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            //try {
//                File root =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                File filepath = new File(root, "image" + ".txt");  // file path to save
//                FileWriter writer = new FileWriter(filepath);
//                writer.append(encodedImage);
//                writer.flush();
//                writer.close();
                image = encodedImage;
                Log.d("ddd", encodedImage);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return "dsd";
        }
    }


}
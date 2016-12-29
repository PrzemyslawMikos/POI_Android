package com.adventure.poi.poi_android;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import additional.LocationHelper;
import additional.RestTaskDelegate;
import additional.SharedPreferencesManager;
import constants.MainConstants;
import entity.PointEntity;
import entity.TypeEntity;
import rest.PointsHelper;
import rest.TypesHelper;

public class AddActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap photo;
    private EditText editName, editDescription;
    private Spinner spinnerTypes;
    private ImageView imagePhoto;
    private Button buttonTakePhoto, buttonSend;
    private LocationHelper locationHelper;
    private TypesHelper th;

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
        spinnerTypes = (Spinner) findViewById(R.id.spinnerTypes);
        imagePhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        buttonSend = (Button) findViewById(R.id.buttonSend);
    }

    public void onPhotoClick(View v){
        dispatchTakePictureIntent();
    }

    public void onSendClick(View v){
        PointsHelper pointsHelper = new PointsHelper(AddActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                Toast t = Toast.makeText(AddActivity.this, "Punkt dodany", Toast.LENGTH_LONG);
                t.show();
            }
        });
        //TODO poczekaj na pobranie lokalizacji !!
        Location loc = locationHelper.getCurrentLocation();
        SharedPreferencesManager prefManager = new SharedPreferencesManager(AddActivity.this);
        PointEntity pe = new PointEntity(loc.getLongitude(), loc.getLatitude(), editName.getText().toString(), editDescription.getText().toString(), "dd", "image/jpeg", 1, Long.valueOf(prefManager.getPreference(MainConstants.PREFERENCE_USERID)));
        pointsHelper.postPoint("Dodawanie punktu", pe);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo = imageBitmap;
            ImageView imageView = (ImageView) findViewById(R.id.imageViewPhoto);
            imageView.setImageBitmap(imageBitmap);
            Log.d("zdj", Integer.toString(photo.getWidth()));
        }
    }

    private String bitmapToString(Bitmap photo){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
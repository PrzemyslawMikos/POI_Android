package com.adventure.poi.poi_android;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import java.util.List;
import additional.CaptureImageHelper;
import additional.SingleLocationHelper;
import additional.PhotoToBase64;
import additional.SharedPreferencesManager;
import constants.MainConstants;
import delegates.LocationDelegate;
import delegates.PhotoToBase64Delegate;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import rest.PointsHelper;
import rest.TypesHelper;

public class AddActivity extends AppCompatActivity implements MainConstants {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private CaptureImageHelper captureImageHelper;
    private EditText editName, editDescription, editLocality;
    private RatingBar ratingBar;
    private Spinner spinnerTypes;
    private ImageView imagePhoto;
    private Button buttonSend;
    private SingleLocationHelper singleLocationHelper;
    private TypesHelper typesHelper;
    private PhotoToBase64 photoToBase64;
    private String photoB64 = null;
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        assignControls();
        singleLocationHelper = new SingleLocationHelper(this, getResources().getString(R.string.location_download), new LocationDelegate() {
            @Override
            public void TaskCompletionResult(Location result) {
                location = result;
                singleLocationHelper.stopLocationManager(getApplicationContext());
                typesHelper.getAllTypes(getResources().getString(R.string.types_downloading));
            }
        });
        typesHelper = new TypesHelper(AddActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                fillSpinner(typesHelper.getTypesNamesList());
            }
        });

        captureImageHelper = new CaptureImageHelper(AddActivity.this);
        captureImageHelper.startTakingPhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            captureImageHelper.onActivityResult(requestCode, resultCode, data);
            imagePhoto.setImageBitmap(captureImageHelper.getPhoto());
            getBase64Photo();
        }
        else{
            this.finish();
        }
    }

    private void getBase64Photo(){
        photoToBase64 = new PhotoToBase64(getApplicationContext(), "Przygotowywanie zdjęcia", new PhotoToBase64Delegate() {
            @Override
            public void TaskCompletionResult(String result) {
                photoB64 = result;
            }
        });
        photoToBase64.execute(captureImageHelper.getPhoto());
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
        buttonSend = (Button) findViewById(R.id.buttonSend);
        ratingBar = (RatingBar) findViewById(R.id.ratingRatingBar);
    }

    public void onSendClick(View v){
        if(photoB64 != null && location != null){
            PointsHelper pointsHelper = new PointsHelper(AddActivity.this, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                }
            });
            SharedPreferencesManager prefManager = new SharedPreferencesManager(AddActivity.this);
            photoB64.replaceAll("[\n\r]", "");
            PointEntity pointEntity = new PointEntity(location.getLongitude(), location.getLatitude(), (double)ratingBar.getRating(), editName.getText().toString(), editLocality.getText().toString(), editDescription.getText().toString(), photoB64, "jpeg", 1, Long.valueOf(prefManager.getPreferenceString(PREFERENCE_USERID)));
            pointsHelper.postPoint("Dodawanie punktu", pointEntity);
        }
        else{

        }
    }

}
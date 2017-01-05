package com.adventure.poi.poi_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.net.URL;

import additional.GeoAddress;
import additional.GoogleLocation;
import additional.ImageManager;
import delegates.GoogleLocationTaskDelegate;
import delegates.ImageTaskDelegate;
import delegates.RestTaskDelegate;
import constants.RestConstants;
import constants.TextsConstants;
import entity.PointEntity;
import rest.PointsHelper;

public class PointActivity extends AppCompatActivity implements RestConstants, TextsConstants {

    private PointsHelper pointsHelper;
    private PointEntity point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.carNavi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.toString(point.getLatitude()) + "," + Double.toString(point.getLongitude()) + "&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        FloatingActionButton footNavi = (FloatingActionButton) findViewById(R.id.footNavi);
        footNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.toString(point.getLatitude()) + "," + Double.toString(point.getLongitude()) + "&mode=w");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        loadPoint();
    }

    private void loadPoint(){
        pointsHelper = new PointsHelper(PointActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                Log.d("point", "tag");
                point = pointsHelper.getPoints().get(0);
                loadImage();
                setContent();
                setGeoAddress(point);
            }
        });
        pointsHelper.getPointById("Pobieranie danych punktu...", Long.valueOf(2));
    }

    private void setGeoAddress(PointEntity point){
        GoogleLocation googleLocation = new GoogleLocation(this, new GoogleLocationTaskDelegate() {
            @Override
            public void TaskCompletionResult(GeoAddress result) {
                if(result != null){

                    TextView location = (TextView) findViewById(R.id.textViewPointLocation);
                    location.setText(result.getCompleteAddress());
                }
            }
        });
        googleLocation.execute(point);
    }

    private void setContent(){
        TextView description = (TextView) findViewById(R.id.textViewPointDescription);
        description.setText(String.format(TEXT_POINT_DESCRIPTION, point.getDescription()));
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(point.getName());
    }

    private void loadImage(){
        ImageManager im = new ImageManager(new ImageTaskDelegate() {
            @Override
            public void TaskCompletionResult(Bitmap result) {
                CollapsingToolbarLayout ll = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                ll.setBackground(new BitmapDrawable(getResources(),result));
            }
        });

        try{
            URL url = new URL(String.format(REST_POINTS_IMAGE, point.getPicture()));
            im.runTask(url);
        }
        catch(Exception e){

        }


    }

}

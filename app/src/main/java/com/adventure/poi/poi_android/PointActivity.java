package com.adventure.poi.poi_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.net.URL;

import additional.GoogleLocation;
import additional.ImageManager;
import additional.ImageTaskDelegate;
import additional.RestTaskDelegate;
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
            }
        });
        pointsHelper.getPointById("Pobieranie danych punktu...", Long.valueOf(2));
    }

    private void setContent(){
        TextView description = (TextView) findViewById(R.id.textViewPointDescription);
        description.setText(String.format(TEXT_POINT_DESCRIPTION, point.getDescription()));

        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(point.getName());

        GoogleLocation googleLocation = new GoogleLocation(point, this);

        TextView location = (TextView) findViewById(R.id.textViewPointLocation);
        location.setText(googleLocation.getAddress().getAddress());
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

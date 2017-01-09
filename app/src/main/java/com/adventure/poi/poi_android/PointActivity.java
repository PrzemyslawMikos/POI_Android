package com.adventure.poi.poi_android;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import java.net.MalformedURLException;
import java.net.URL;
import additional.GeoAddress;
import additional.GoogleLocation;
import additional.GoogleNavi;
import additional.ImageManager;
import constants.MainConstants;
import delegates.GoogleLocationTaskDelegate;
import delegates.ImageTaskDelegate;
import constants.RestConstants;
import constants.TextsConstants;
import entity.PointEntity;
import entity.TypeEntity;

public class PointActivity extends AppCompatActivity implements RestConstants, MainConstants, TextsConstants {

    private Toolbar toolbar;
    private TextView textPointLocation, textPointDescription, textPointName, textPointLocality, textPointAddeddate, textTypeName, textTypeDescription, textTypeAddeddate;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private PointEntity point;
    private TypeEntity type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        assignControls();
        setExtras();
        fillControls();
    }

    private void assignControls(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textPointLocation = (TextView) findViewById(R.id.textViewPointLocation);
        textPointName = (TextView) findViewById(R.id.textViewPointName);
        textPointDescription = (TextView) findViewById(R.id.textViewPointDescription);
        textPointLocality = (TextView) findViewById(R.id.textViewPointLocality);
        textPointAddeddate = (TextView) findViewById(R.id.textViewPointAddeddate);
        textTypeName = (TextView) findViewById(R.id.textViewTypeName);
        textTypeDescription = (TextView) findViewById(R.id.textViewTypeDescription);
        textTypeAddeddate = (TextView) findViewById(R.id.textViewTypeAddeddate);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
    }

    private void setExtras(){
        try{
            point = (PointEntity) this.getIntent().getSerializableExtra(SERIAlIZABLE_POINT);
            type = (TypeEntity) this.getIntent().getSerializableExtra(SERIAlIZABLE_TYPE);
        }
        catch (NullPointerException e){
            this.finish();
        }
    }

    private void fillControls(){
        setGeoAddress(point);
        loadImage();
        collapsingToolbarLayout.setTitle(point.getName());
        textPointName.setText(point.getName());
        textPointDescription.setText(point.getDescription());
        textPointLocality.setText(point.getLocality());
        textPointAddeddate.setText(point.getAddeddate());
        textTypeName.setText(type.getName());
        textTypeDescription.setText(type.getDescription());
        textTypeAddeddate.setText(type.getAddeddate());
    }

    public void onFloatingCarNaviClick(View v){
        GoogleNavi.startNavi(GOOGLE_NAVI_CAR, PointActivity.this, point);
    }

    public void onFloatingFootNaviClick(View v){
        GoogleNavi.startNavi(GOOGLE_NAVI_FOOT, PointActivity.this, point);
    }

    private void loadImage(){
        ImageManager im = new ImageManager(new ImageTaskDelegate() {
            @Override
            public void TaskCompletionResult(Bitmap result) {
                collapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(),result));
            }
        });

        try{
            URL url = new URL(String.format(REST_POINTS_IMAGE, point.getPicture()));
            im.runTask(url);
        }
        catch(MalformedURLException e){
            Snackbar.make(findViewById(R.id.activity_point), R.string.point_bad_url, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setGeoAddress(PointEntity point){
        GoogleLocation googleLocation = new GoogleLocation(this, new GoogleLocationTaskDelegate() {
            @Override
            public void TaskCompletionResult(GeoAddress result) {
                if(result != null){
                    textPointLocation.setText(result.getCompleteAddress());
                }
            }
        });
        googleLocation.execute(point);
    }
}
package com.adventure.poi.poi_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.MapPointDialog;
import additional.NetworkStateManager;
import additional.PermissionHelper;
import additional.SnackbarManager;
import constants.MainConstants;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import rest.PointsHelper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, MainConstants {

    private boolean locationServiceEnabled;
    private LocationManager locationManager;
    private Location userLocation = null;
    private ArrayList<PointEntity> listPoints = null;
    public ArrayList<Marker> listMarkers = null;
    private PointsHelper pointsHelper;
    private TextView textViewDistance;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private GoogleMap mMap;
    private int selectedDistance = 50;
    private Circle userCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        assignControls();
    }

    private void assignControls() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        // TODO sprawdzanie
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50,50, this);
        listMarkers = new ArrayList<>();
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        setDistanceSeekBar();
        setSlidingPanel();
        setSearchButton();
        locationServiceEnabled = isLocationProviderEnabled();
        checkLocationProvider();
    }

    private boolean isLocationProviderEnabled(){
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }
        else{
            return false;
        }
    }

    private void setDistanceSeekBar(){
        SeekBar seekBarDistance = (SeekBar) findViewById(R.id.seek_bar_distance);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewDistance.setText(String.format(getResources().getString(R.string.map_menu_distance), GOOGLE_MIN_DISTANCE + i));
                selectedDistance = GOOGLE_MIN_DISTANCE + i;
                if(userCircle != null){
                    userCircle.setRadius(selectedDistance);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSlidingPanel(){
        ImageView imageViewArrow = (ImageView) findViewById(R.id.slide_arrow);
        imageViewArrow.setImageResource(R.drawable.ic_arrow_up);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slide_panel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                ImageView imageViewArrow = (ImageView) findViewById(R.id.slide_arrow);
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    imageViewArrow.setImageResource(R.drawable.ic_arrow_down);
                }
                else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    imageViewArrow.setImageResource(R.drawable.ic_arrow_up);
                }
            }
        });
    }

    private void checkLocationProvider(){
        if(!locationServiceEnabled) {
            SnackbarManager.showSnackbarWithSettings(MapActivity.this, getResources().getString(R.string.map_request_location), Snackbar.LENGTH_LONG);
        }
    }

    private void setSearchButton(){
        Button buttonSearchDistance = (Button) findViewById(R.id.buttonSearchDistance);
        buttonSearchDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Location", "enabled");
                if(locationServiceEnabled) {
                    pointsHelper = new PointsHelper(MapActivity.this, new RestTaskDelegate() {
                        @Override
                        public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                            listPoints = pointsHelper.getPoints();
                            removeMarkers(listMarkers);
                            listMarkers.clear();
                            for (PointEntity point: listPoints) {
                                addToMap(point);
                            }
                        }
                    });
                    if(userLocation != null){
                        pointsHelper.getPointsDistance(String.format(getResources().getString(R.string.map_searching_dialog), selectedDistance) , userLocation.getLatitude(), userLocation.getLongitude(), selectedDistance);
                    }
                }
                else{
                    SnackbarManager.showSnackbarWithSettings(MapActivity.this, getResources().getString(R.string.map_request_location), Snackbar.LENGTH_LONG);
                }
            }
        });
    }

    private void removeMarkers(ArrayList<Marker> listMarkers){
        if(listMarkers != null){
            for(Marker marker : listMarkers){
                marker.remove();
            }
        }
    }

    private void addToMap(PointEntity pointEntity){
        Marker m = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .anchor(0.0f, 1.0f)
                .position(new LatLng(pointEntity.getLatitude(), pointEntity.getLongitude())));
        listMarkers.add(m);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (PermissionHelper.checkLocationPermission(getApplicationContext())) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
    }

    private void setUserMapCircle(){
        if(userLocation != null){
            userCircle = mMap.addCircle(new CircleOptions().center(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())).radius(selectedDistance).strokeColor(Color.argb(70, 60, 78, 252)).fillColor(Color.argb(70, 160, 168, 242)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), GOOGLE_MAP_ZOOM));
        }
    }

    @Override
    public void onBackPressed(){
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", "center");
        userLocation = location;
        if(userCircle == null){
            setUserMapCircle();
        }
        else {
            userCircle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        locationServiceEnabled = true;
    }

    @Override
    public void onProviderDisabled(String s) {
        locationServiceEnabled = false;
        checkLocationProvider();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(NetworkStateManager.isNetworkAvailable(MapActivity.this)){
            if(listMarkers.contains(marker))
            {
                MapPointDialog mapPointDialog = new MapPointDialog(MapActivity.this, listPoints.get(listMarkers.indexOf(marker)));
                mapPointDialog.show();
            }
        }else{
            SnackbarManager.showSnackbarWithSettings(MapActivity.this, getResources().getString(R.string.enable_network_text), Snackbar.LENGTH_LONG);
        }
        return false;
    }
}
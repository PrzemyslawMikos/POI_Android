package com.adventure.poi.poi_android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import additional.PointRatingDialog;
import constants.MainConstants;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import rest.PointsHelper;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, MainConstants {

    private Location userLocation;
    private LocationManager locationManager;
    private ArrayList<PointEntity> listPoints = null;
    public ArrayList<Marker> listMarkers = null;
    private PointsHelper pointsHelper;
    private SeekBar seekBarDistance;
    private TextView textViewDistance;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private GoogleMap mMap;
    private Button buttonSearchDistance;
    private final int minDistance = 50;
    private int selectedDistance = 50;
    private Circle userCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        assignControls();
    }

    private void assignControls() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        // TODO sprawdzanie
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 50, this);

        listMarkers = new ArrayList<>();
        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        final SeekBar seekBarDistance = (SeekBar) findViewById(R.id.seek_bar_distance);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewDistance.setText(String.format(getResources().getString(R.string.map_menu_distance), minDistance+i));
                selectedDistance = minDistance + i;
                userCircle.setRadius(selectedDistance);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

        buttonSearchDistance = (Button) findViewById(R.id.buttonSearchDistance);
        buttonSearchDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Wybrana odleglosc", Integer.toString(selectedDistance));
                pointsHelper = new PointsHelper(MapActivity.this, new RestTaskDelegate() {
                    @Override
                    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                        listPoints = pointsHelper.getPoints();
                        removeMarkers(listMarkers);
                        listMarkers.clear();
                        for (PointEntity point: listPoints) {
                            addToMap(point);
                            Log.d("POINT DISTANCE",point.getDistance().toString());
                        }
                    }
                });
                pointsHelper.getPointsDistance("Wyszukiwanie w odleglosci" + Integer.toString(selectedDistance), 50.6938508, 17.2834485, selectedDistance);
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
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(pointEntity.getLatitude(), pointEntity.getLongitude())));
        listMarkers.add(m);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Ustawienie markera własnej lokalizacji
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        userCircle = mMap.addCircle(new CircleOptions().center(new LatLng(50.6938508, 17.2834485)).radius(selectedDistance).strokeColor(Color.argb(70, 60, 78, 252)).fillColor(Color.argb(70, 160, 168, 242)));

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
        if(userLocation == null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), GOOGLE_MAP_ZOOM));
        }
        userLocation = location;
        userCircle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        // TODO ustawić mapę na lokalizację użytkownika
    }

    @Override
    public void onProviderDisabled(String s) {
        // wyłączyć opcje wyszukiwanie, komunikat aby włączyć lokalizacje
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(listMarkers.contains(marker))
        {
            MapPointDialog mapPointDialog = new MapPointDialog(MapActivity.this, listPoints.get(listMarkers.indexOf(marker)));
            mapPointDialog.show();
        }

        return false;
    }
}

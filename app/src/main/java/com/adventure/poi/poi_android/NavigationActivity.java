package com.adventure.poi.poi_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.TypesRowAdapter;
import additional.PointsRowAdapter;
import delegates.RestTaskDelegate;
import additional.SharedPreferencesManager;
import constants.MainConstants;
import constants.RestConstants;
import entity.PointEntity;
import entity.TypeEntity;
import entity.UserEntity;
import rest.PointsHelper;
import rest.TypesHelper;
import rest.UsersHelper;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestConstants, MainConstants{

    private TypesHelper typesHelper;
    private UsersHelper usersHelper;
    private PointsHelper pointsHelper;
    private TypeEntity selectedType = null;
    private ArrayList<TypeEntity> listTypes = null;
    private ArrayList<PointEntity> listPoints = null;
    private FloatingActionButton addPointFloatingActionButton;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private LinearLayout linearLayout;
    private String selectedLocality;
    private int offset = 0;
    private int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        assignControls();
        getTypes();
    }

    private void assignControls(){
        addPointFloatingActionButton = (FloatingActionButton) findViewById(R.id.addPointFloatingActionButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.content_navigation);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setListeners();
    }

    private void setListeners(){
        addPointFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clearContentView(){
        linearLayout.removeAllViews();
    }

    private void getTypes(){
        if(listTypes != null){
            fillListTypes();
        }
        else{
            typesHelper = new TypesHelper(NavigationActivity.this, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    listTypes = typesHelper.getTypes();
                    fillListTypes();
                }
            });
            typesHelper.getAllTypes(getResources().getString(R.string.types_downloading));
        }
        setTitle(getResources().getString(R.string.navigation_activity_type_title));
    }

    public void fillListTypes(){
        clearContentView();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.view_types, null);
        linearLayout.addView(view);
        TypesRowAdapter adapter = new TypesRowAdapter(this, R.layout.row_list_types, listTypes);
        ListView listViewTypes = (ListView) findViewById(R.id.listViewTypes);
        listViewTypes.setAdapter(adapter);
        listViewTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedType = listTypes.get(position);
                selectedLocality = DEFAULT_POINTS_LOCALITY_PARAM;
                getPoints(selectedType, selectedLocality);
            }
        });
    }

    private void getPoints(TypeEntity type, String locality){
        setTitle(String.format(getResources().getString(R.string.navigation_activity_point_title), selectedLocality.equals(DEFAULT_POINTS_LOCALITY_PARAM) ? getResources().getString(R.string.all_points) : selectedLocality));
        pointsHelper = new PointsHelper(NavigationActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                listPoints = pointsHelper.getPoints();
                fillListPoints(listPoints);
            }
        });
        pointsHelper.getPointsCriteria(getResources().getString(R.string.point_searching), type.getId(), locality, limit, offset);
    }

    private void createPointView(){
        clearContentView();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        view = inflater.inflate(R.layout.view_points, null);
        linearLayout.addView(view);
        Button searchPointsButton = (Button) findViewById(R.id.buttonSearchPoints);
        searchPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextPointLocality = (EditText) findViewById(R.id.editTextPointLocality);
                selectedLocality = editTextPointLocality.getText().toString();
                if(!selectedLocality.isEmpty()){
                    getPoints(selectedType, selectedLocality);
                }
                else{
                    selectedLocality = DEFAULT_POINTS_LOCALITY_PARAM;
                    getPoints(selectedType, selectedLocality);
                }
            }
        });
    }
//TODO ulepszyć
    private void fillListPoints(ArrayList<PointEntity> points){
        if(findViewById(R.id.view_points) == null){
            createPointView();
        }
        PointsRowAdapter adapter = new PointsRowAdapter(this, R.layout.row_list_points, points);
        ListView listViewPoints = (ListView) findViewById(R.id.listViewPoints);
        listViewPoints.setAdapter(adapter);
        listViewPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPoint(position);
            }
        });

        Button nextButton = (Button) findViewById(R.id.buttonNextPage);
        Button previousButton = (Button) findViewById(R.id.buttonPreviousPage);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //offset += 10;
                EditText editTextPointLocality = (EditText) findViewById(R.id.editTextPointLocality);
                getPoints(selectedType, selectedLocality);
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showPoint(int position){
        if(listPoints.get(position) != null){
            Intent intent = new Intent(NavigationActivity.this, PointActivity.class);;
            intent.putExtra(SERIAlIZABLE_POINT, listPoints.get(position));
            intent.putExtra(SERIAlIZABLE_TYPE, selectedType);
            startActivity(intent);
        }
    }

    private void logout(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.unsetKey(MainConstants.PREFERENCE_REMEMBER_ME);
        sharedPreferencesManager.unsetCredentials();
        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if((findViewById(R.id.view_points) != null) || (findViewById(R.id.info_user) != null)){
            getTypes();
        }
        else {
            super.onBackPressed();
        }
    }

    private void displayUserData(){
        usersHelper = new UsersHelper(NavigationActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                showUserData(usersHelper.getUsers().get(0));
            }
        });
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        usersHelper.getUserById(getResources().getString(R.string.downloading_user_data), sharedPreferencesManager.getPreferenceString(MainConstants.PREFERENCE_USERID));
    }

    private void showUserData(UserEntity user){
        clearContentView();
        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView;
        customView = linflater.inflate(R.layout.info_user, null);
        linearLayout.addView(customView);
        // TODO udoskonalić wyświetlanie danych użytkownika
        TextView textView = (TextView) findViewById(R.id.textViewUserInfo);
        textView.setText("Nick: " + user.getNickname() + "\nNazwa użytkownika: " + user.getUsername() + "\nEmail: " + user.getEmail() + "\nTelefon: " + user.getPhone() + "\nData rejestracji: " + user.getCreationdate());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_category) {
            getTypes();
        }else if (id == R.id.nav_google_map){
            Intent intent = new Intent(NavigationActivity.this, MapActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_user_data) {
            displayUserData();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
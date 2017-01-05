package com.adventure.poi.poi_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.ArrayList;

import additional.ImageManager;
import delegates.ImageTaskDelegate;
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

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.Toast.makeText;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestConstants, MainConstants{
    private TypesHelper th;
    private UsersHelper usersHelper;
    private PointsHelper ph;
    private ImageView views;
    private int currentFirstVisibleItem, currentVisibleItemCount, currentScrollState;
    private int margin = 0;
    private int offset = 0;
    private int limit = 10;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getTypes();
    }

    private void getTypes(){
        th = new TypesHelper(NavigationActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                clearContentView();
                fillList(th.getTypes());
            }
        });
        th.getAllTypes("pobieranie typów");
    }

    public void fillList(ArrayList<TypeEntity> typelist){
        try{
            ImageManager im = new ImageManager(new ImageTaskDelegate() {
                @Override
                public void TaskCompletionResult(Bitmap result) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.content_navigation);
                    ImageButton views = new ImageButton(NavigationActivity.this);
                    views.setImageBitmap(result);
                    views.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast t = Toast.makeText(NavigationActivity.this, "bang", Toast.LENGTH_LONG);
                            t.show();
                        }
                    });
                    ll.addView(views);
                }
            });

            //TextView tv = (TextView) findViewById(R.id.textView2);
            for(TypeEntity type:typelist ){
                URL url = new URL(String.format(REST_TYPES_IMAGE, type.getImage()));
                //tv.setText( tv.getText().toString() + String.format("\n%1$d: %2$s, %3$s aefnwnefjqnwejflhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjdsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssfafadsfsadfasdfaegjyu;p;oilukythrefwqderhtykuli;olukyjthrgefwrhtykuli;o'hlugykthrgewfrhtykulijjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjnqjwklfnqjkwlneflkjqwnekfjlqnwelkjfnqwkljenfkljqwnekfjlnqwkejfnkqjwlnefkljqwnjkelfnqklwjenfkljqwnekfjlnqwkjlenflqkwjenflqwnelfjqwnlkefnkljwqenfjklnqwkejnfkjqwnefjkqnlkwjneflkqjwnelfkjqnlwkejnflqkjwnef", type.getId(), type.getName(), type.getAddeddate()));
                im.runTask(url);
            }




        }
        catch(Exception e){

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getTypes();
        } else if (id == R.id.nav_gallery) {
            showPoint();
        } else if (id == R.id.nav_slideshow) {
            showListPointss();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            displayUserData();

        } else if (id == R.id.nav_send) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void showListPointss(){
       ph = new PointsHelper(NavigationActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                showListPoints(ph.getPoints());
            }
        });
        ph.getPointsCriteria("Wyszukiwanie punktów", 1, "Miejscowosc", limit, offset);
    }

    private void showListPoints(ArrayList<PointEntity> points){
        clearContentView();
        LinearLayout ll = (LinearLayout) findViewById(R.id.content_navigation);
        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView;
        customView = linflater.inflate(R.layout.view_points, null);
        ll.addView(customView);
        PointsRowAdapter adapter = new PointsRowAdapter(this, R.layout.row_list_points, points);
        ListView Lview = (ListView) findViewById(R.id.listViewPoints);
        Lview.setAdapter(adapter);
        Lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NavigationActivity.this, "kliknieto: " + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

        Lview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                currentScrollState = scrollState;
                isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
            }
        });


        Button btn = (Button) findViewById(R.id.buttonNextPage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset += 10;
                showListPointss();
            }
        });
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
            limit += 10;
            showListPointss();
        }
    }

    private void showPoint(){
        Intent intent = new Intent(NavigationActivity.this, PointActivity.class);
        startActivity(intent);
    }

    private void clearContentView(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.content_navigation);
        ll.removeAllViews();
    }

    private void displayUserData(){
        usersHelper = new UsersHelper(NavigationActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                showUserData(usersHelper.getUsers().get(0));
            }
        });
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        usersHelper.getUserById("Pobieranie danych użytkownika", sharedPreferencesManager.getPreference(MainConstants.PREFERENCE_USERID));
    }

    private void showUserData(UserEntity user){
        clearContentView();
        LinearLayout ll = (LinearLayout) findViewById(R.id.content_navigation);
        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView;
        customView = linflater.inflate(R.layout.info_user, null);
        ll.addView(customView);
        TextView textView = (TextView) findViewById(R.id.textViewUserInfo);
        textView.setText("Nick: " + user.getNickname() + "\nNazwa użytkownika: " + user.getUsername() + "\nEmail: " + user.getEmail() + "\nTelefon: " + user.getPhone() + "\nData rejestracji: " + user.getCreationdate());
    }



    private void logout(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.unsetCredentials();
        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }
}
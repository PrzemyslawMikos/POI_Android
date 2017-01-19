package com.adventure.poi.poi_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.MenuHelper;
import additional.PointsRowAdapter;
import additional.SharedPreferencesManager;
import additional.TypesRowAdapter;
import constants.MainConstants;
import constants.RestConstants;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import entity.TypeEntity;
import rest.PointsHelper;
import rest.TypesHelper;

public class MenuActivity extends AppCompatActivity implements RestConstants, MainConstants{

    private TextView textMenu;
    private EditText editLocality;
    private ImageButton searchButton;
    private ArrayList<PointEntity> listPoints = null;
    private ArrayList<TypeEntity> listTypes = null;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private PointsRowAdapter pAdapter;
    private TypesHelper typesHelper;
    private PointsHelper pointsHelper;
    private TypeEntity selectedType;
    private String selectedLocality = "";
    private ListView listView;
    private MenuHelper menuHelper;
    private boolean flagLoading = false;
    private int mode = 1;
    private int offset = 0;
    private int limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        assignControls();
        getTypes();
    }

    private void assignControls(){
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.menu_slide_panel);
        listView = (ListView) findViewById(R.id.listView);
        textMenu = (TextView) findViewById(R.id.text_main_manu);
        editLocality = (EditText) findViewById(R.id.editTextLocalityName);
        searchButton = (ImageButton) findViewById(R.id.menu_search_button);
        menuHelper = new MenuHelper(MenuActivity.this);
        setMenuListeners();
        setButtonListener();
    }

    private void setButtonListener(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! selectedLocality.equals(editLocality.getText().toString())){
                    selectedLocality = editLocality.getText().toString();
                    switch (mode){
                        case 1:
                            resetOffsetLimit();
                            getPoints(NO_TYPE, selectedLocality);
                            mode = 2;
                            textMenu.setText(String.format(getResources().getString(R.string.mainmenu_points_title), getResources().getString(R.string.mainmenu_none), selectedLocality));
                            break;
                        case 2:
                            resetOffsetLimit();
                            if(selectedType != null){
                                flagLoading = false;
                                getPoints(selectedType.getId(), selectedLocality);
                                textMenu.setText(String.format(getResources().getString(R.string.mainmenu_points_title), selectedType.getName(), selectedLocality));
                            }
                            else {
                                flagLoading = false;
                                getPoints(NO_TYPE, selectedLocality);
                                textMenu.setText(String.format(getResources().getString(R.string.mainmenu_points_title), getResources().getString(R.string.mainmenu_none), selectedLocality));
                            }
                            break;
                    }
                }
            }

        });
    }

    private void setMenuListeners(){
        menuHelper.getById(R.id.manu_types_element).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTypes();
                setTypesMode();
            }
        });
        menuHelper.getById(R.id.manu_google_element).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        menuHelper.getById(R.id.manu_add_element).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        menuHelper.getById(R.id.manu_user_data_element).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        menuHelper.getById(R.id.manu_logout_element).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void getTypes(){
        if(listTypes != null){
            fillListTypes();
        }
        else{
            typesHelper = new TypesHelper(MenuActivity.this, new RestTaskDelegate() {
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
        TypesRowAdapter adapter = new TypesRowAdapter(this, R.layout.row_list_types, listTypes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectedType = listTypes.get(position);
                selectedLocality = DEFAULT_POINTS_LOCALITY_PARAM;
                getPoints(selectedType.getId(), selectedLocality);
                mode = 2;
                textMenu.setText(String.format(getResources().getString(R.string.mainmenu_points_title), selectedType.getName(), selectedLocality));
            }
        });
    }

    private void getPoints(int typeid, String locality){
        setTitle(String.format(getResources().getString(R.string.navigation_activity_point_title), selectedLocality.equals(DEFAULT_POINTS_LOCALITY_PARAM) ? getResources().getString(R.string.all_points) : selectedLocality));
        pointsHelper = new PointsHelper(MenuActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                listPoints = pointsHelper.getPoints();
                fillListPoints(listPoints);
            }
        });
        pointsHelper.getPointsCriteria(getResources().getString(R.string.point_searching), typeid, locality, limit, offset);
    }

    private void getMorePoints(int typeid, String locality){
        pointsHelper = new PointsHelper(MenuActivity.this, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                listPoints = pointsHelper.getPoints();
                pAdapter.addToList(listPoints);
                pAdapter.notifyDataSetChanged();
                flagLoading = false;
            }
        });
        pointsHelper.getPointsCriteria(getResources().getString(R.string.point_searching), typeid, locality, limit, offset);
    }

    private void fillListPoints(ArrayList<PointEntity> points){
        pAdapter = new PointsRowAdapter(this, R.layout.row_list_points, points);
        listView.setAdapter(pAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                showPoint(position);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!flagLoading) {
                        flagLoading = true;
                        offset += limit;
                        if(selectedType != null) {
                            getMorePoints(selectedType.getId(), selectedLocality);
                        }else{
                            getMorePoints(NO_TYPE, selectedLocality);
                        }
                    }
                }
            }
        });
    }

    private void showPoint(int position){
        if(pAdapter.getList().get(position) != null){
            Intent intent = new Intent(MenuActivity.this, PointActivity.class);;
            intent.putExtra(SERIAlIZABLE_POINT, pAdapter.getList().get(position));
            if(selectedType != null){
                intent.putExtra(SERIAlIZABLE_TYPE, selectedType);
            }
            else{
                TypeEntity typeEntity = listTypes.get(0);
                for (TypeEntity type: listTypes) {
                    if(type.getId() == pAdapter.getList().get(position).getTypeid())
                        typeEntity = type;
                }
                intent.putExtra(SERIAlIZABLE_TYPE, typeEntity);
            }
            startActivity(intent);
        }
    }

    private void logout(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.unsetKey(MainConstants.PREFERENCE_REMEMBER_ME);
        sharedPreferencesManager.unsetCredentials();
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }

    private void resetLimits(){
        resetOffsetLimit();
        selectedLocality = "";
        selectedType = null;
        flagLoading = false;
    }

    private void resetOffsetLimit(){
        offset = 0;
        limit = 10;
    }

    @Override
    public void onBackPressed(){
        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else if(mode == 2){
            setTypesMode();
        }
        else{
            super.onBackPressed();
        }
    }

    private void setTypesMode(){
        if(listTypes != null && !listTypes.isEmpty()){
            fillListTypes();
        }
        else{
            getTypes();
        }
        resetLimits();
        textMenu.setText(getResources().getString(R.string.mainmenu_type_title));
        mode = 1;
    }
}
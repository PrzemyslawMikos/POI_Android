package rest;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.SharedPreferencesManager;
import additional.TaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.PointEntity;
import entity.StatusEntity;

/**
 * Created by Przemek on 12.12.2016.
 */

public class PointsHelper implements RestConstants, MainConstants{

    private Activity activity;
    private RestHelper restHelper;
    private ArrayList<PointEntity> points;
    private StatusEntity statusEntity;
    private int action;
    private TaskDelegate delegate;

    public PointsHelper(Activity activity, TaskDelegate delegate) {
        this.activity = activity;
        this.points = new ArrayList<>();
        this.delegate = delegate;
    }

    public ArrayList<PointEntity> getPoints() {
        return points;
    }

    public void getPointById(String message, Long id){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(String.format(REST_POINTS_GET_ID, id), HttpMethod.GET, header, activity, message, new TaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if(restHelper.getResult()) {
                    if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                        String spoint = restHelper.getResponseEntity().getBody();
                        JSONObject jpoint= new JSONObject(spoint);
                        PointEntity point = new PointEntity(jpoint);
                        points.add(point);
                    }
                }
                else{
                    Toast.makeText(activity, "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
                }
                delegate.TaskCompletionResult(restHelper.getResponseEntity());
            }
        });
        restHelper.runTask();
    }

    public void postPoint(String message, PointEntity point){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(REST_POINTS_POST, HttpMethod.POST, header, point.toJSON(), activity, message, new TaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if(restHelper.getResult()) {
                    if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                        statusEntity = new StatusEntity(restHelper.getResponseEntity().getBody());
                    }
                }
                else{
                    Toast.makeText(activity, "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
                }
                delegate.TaskCompletionResult(restHelper.getResponseEntity());
            }
        });
        restHelper.runTask();
    }

    private HttpHeaders getHeaderWithBearer(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(REQUEST_HEADER_AUTHORIZATION, String.format(REQUEST_HEADER_BEARER, this.getToken()));
        Log.d("bearer", String.format(REQUEST_HEADER_BEARER, this.getToken()));
        return headers;
    }

    private String getToken(){
        SharedPreferencesManager prefManager = new SharedPreferencesManager(activity);
        return prefManager.getPreference(PREFERENCE_TOKEN);
    }

}
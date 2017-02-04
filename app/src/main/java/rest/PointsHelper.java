package rest;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import com.adventure.poi.poi_android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.SnackbarManager;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import entity.StatusEntity;

/**
 * Created by Przemysław Mikos on 12.12.2016.
 */

public class PointsHelper extends EntityHelper{

    private ArrayList<PointEntity> points;

    public PointsHelper(Activity activity, RestTaskDelegate delegate) {
        super(activity, delegate);
        this.points = new ArrayList<>();
    }

    public ArrayList<PointEntity> getPoints() {
        return points;
    }

    public void getPointsCriteria(String message, int typeid, String locality, int limit, int offset, boolean dialogCancelable){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(String.format(REST_POINTS_CRITERIA, typeid, locality, limit, offset), HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    String spointsarray = restHelper.getResponseEntity().getBody();
                    JSONArray jpointsarray= new JSONArray(spointsarray);
                    for (int i = 0; i < jpointsarray.length(); i++) {
                        JSONObject jpoint = jpointsarray.getJSONObject(i);
                        PointEntity type = new PointEntity(jpoint);
                        points.add(type);
                    }
                    PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
                }
                else{
                    showMessages(restHelper.getStatus());
                }
            }
        }, dialogCancelable);
        restHelper.runTask();
    }

    public void getPointsDistance(String message, double latitude, double longitude, int distance, boolean dialogCancelable){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(String.format(REST_POINTS_DISTANCE, latitude, longitude, distance), HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    String spointsarray = restHelper.getResponseEntity().getBody();
                    JSONArray jpointsarray= new JSONArray(spointsarray);
                    for (int i = 0; i < jpointsarray.length(); i++) {
                        JSONObject jpoint = jpointsarray.getJSONObject(i);
                        PointEntity type = new PointEntity(jpoint);
                        points.add(type);
                    }
                    PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
                }
                else{
                    showMessages(restHelper.getStatus());
                }
            }
        }, dialogCancelable);
        restHelper.runTask();
    }

    public void postPoint(String message, PointEntity point, boolean dialogCancelable){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(REST_POINTS_POST, HttpMethod.POST, header, point.toJSON(), super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    showMessages(new StatusEntity(restHelper.getResponseEntity().getBody()));
                    PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
                }
                else{
                    showMessages(restHelper.getStatus());
                }
            }
        }, dialogCancelable);
        restHelper.runTask();
    }

    private void showMessages(StatusEntity entity) {
        switch (entity.getStatus()){
            case STATUS_OK:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.point_added), Snackbar.LENGTH_LONG);
                break;
            case STATUS_NOT_FOUND:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.point_criteria_not_found), Snackbar.LENGTH_LONG);
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.server_exception), Snackbar.LENGTH_LONG);
                break;
        }
    }

}
package rest;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.adventure.poi.poi_android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.ToastManager;
import delegates.RestTaskDelegate;
import entity.PointEntity;
import entity.StatusEntity;

/**
 * Created by Przemek on 12.12.2016.
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

//    public void getPointById(String message, Long id){
//        HttpHeaders header = getHeaderWithBearer();
//        restHelper = new RestHelper(String.format(REST_POINTS_GET_ID, id), HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
//            @Override
//            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
//
//                    if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
//                        String spoint = restHelper.getResponseEntity().getBody();
//                        JSONObject jpoint= new JSONObject(spoint);
//                        PointEntity point = new PointEntity(jpoint);
//                        points.add(point);
//                    }
//
//                else{
//
//                }
//                PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
//            }
//        });
//        restHelper.runTask();
//    }

    public void getPointsCriteria(String message, int typeid, String locality, int limit, int offset){
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
        });
        restHelper.runTask();
    }

    public void postPoint(String message, PointEntity point){
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
        });
        restHelper.runTask();
    }

    private void showMessages(StatusEntity entity) {
        switch (entity.getStatus()){
            case STATUS_OK:
                ToastManager.showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.point_added), Toast.LENGTH_LONG);
                break;
            case STATUS_NOT_FOUND:
                ToastManager.showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.point_criteria_not_found), Toast.LENGTH_LONG);
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                ToastManager.showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.server_exception), Toast.LENGTH_LONG);
                break;
        }
    }

}
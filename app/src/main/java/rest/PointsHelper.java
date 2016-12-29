package rest;

import android.app.Activity;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.RestTaskDelegate;
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

    public void getPointById(String message, Long id){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(String.format(REST_POINTS_GET_ID, id), HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
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
                    Toast.makeText(PointsHelper.super.getActivity(), "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
                }
                PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
            }
        });
        restHelper.runTask();
    }

    public void postPoint(String message, PointEntity point){
        HttpHeaders header = getHeaderWithBearer();
        restHelper = new RestHelper(REST_POINTS_POST, HttpMethod.POST, header, point.toJSON(), super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if(restHelper.getResult()) {
                    if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                        PointsHelper.super.setStatusEntity(new StatusEntity(restHelper.getResponseEntity().getBody()));
                    }
                }
                else{
                    Toast.makeText(PointsHelper.super.getActivity(), "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
                }
                PointsHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
            }
        });
        restHelper.runTask();
    }

}
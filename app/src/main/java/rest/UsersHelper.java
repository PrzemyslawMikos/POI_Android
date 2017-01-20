package rest;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import additional.SnackbarManager;
import delegates.RestTaskDelegate;
import entity.StatusEntity;
import entity.UserEntity;

/**
 * Created by Przemysław Mikos on 28.12.2016.
 */

public class UsersHelper extends EntityHelper {

    private ArrayList<UserEntity> users;

    public UsersHelper(Activity activity, RestTaskDelegate delegate) {
        super(activity, delegate);
        this.users = new ArrayList<>();
    }

    public ArrayList<UserEntity> getUsers() {
        return users;
    }

    public void getUserById(String message, String id){
        HttpHeaders header = super.getHeaderWithBearer();
        restHelper = new RestHelper(String.format(REST_USERS_GET_ID, Long.valueOf(id)), HttpMethod.GET, header, super.getActivity(), message, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if (restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK) {
                    String suser = restHelper.getResponseEntity().getBody();
                    JSONObject juser= new JSONObject(suser);
                    UserEntity userEntity = new UserEntity(juser);
                    users.add(userEntity);
                    UsersHelper.super.getDelegate().TaskCompletionResult(restHelper.getResponseEntity());
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
            case STATUS_NOT_FOUND:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.user_not_found), Snackbar.LENGTH_LONG);
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                SnackbarManager.showSnackbar(activity, activity.getResources().getString(R.string.server_exception), Snackbar.LENGTH_LONG);
                break;
        }
    }

}
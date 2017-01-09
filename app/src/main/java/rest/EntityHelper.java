package rest;

import android.app.Activity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import additional.SharedPreferencesManager;
import delegates.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.StatusEntity;

/**
 * Created by Przemek on 13.12.2016.
 */

public class EntityHelper implements RestConstants, MainConstants {
    private StatusEntity statusEntity;
    private RestTaskDelegate delegate;
    protected Activity activity;
    protected RestHelper restHelper;

    protected EntityHelper(Activity activity, RestTaskDelegate delegate) {
        this.activity = activity;
        this.delegate = delegate;
    }

    protected StatusEntity getStatusEntity() {
        return statusEntity;
    }

    protected void setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
    }

    protected RestTaskDelegate getDelegate() {
        return delegate;
    }

    protected Activity getActivity() {
        return activity;
    }

    protected void setActivity(Activity activity) {
        this.activity = activity;
    }

    protected HttpHeaders getHeaderWithBearer(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(REQUEST_HEADER_AUTHORIZATION, String.format(REQUEST_HEADER_BEARER, this.getToken()));
        return headers;
    }

    private String getToken(){
        SharedPreferencesManager prefManager = new SharedPreferencesManager(activity);
        return prefManager.getPreferenceString(PREFERENCE_TOKEN);
    }
}
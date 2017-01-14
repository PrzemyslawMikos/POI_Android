package rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import additional.NetworkStateManager;
import additional.SharedPreferencesManager;
import constants.MainConstants;
import constants.RestConstants;
import delegates.RestTaskDelegate;
import entity.StatusEntity;
import entity.TokenEntity;

/**
 * Created by Przemek on 04.12.2016.
 */

public class RestHelper implements RestTaskDelegate, RestConstants, MainConstants {

    private String urlPath;
    private HttpMethod requestMethod;
    private HttpHeaders requestHeader;
    private JSONObject requestBody;
    private Activity activity;
    private String loadDialogMessage;
    private ResponseEntity<String> responseEntity;
    private RestTaskDelegate delegate;
    private StatusEntity status = null;

    public RestHelper(String urlPath, HttpMethod requestMethod, HttpHeaders requestHeader, JSONObject requestBody, Activity activity, String loadDialogMessage, RestTaskDelegate delegate) {
        this.urlPath = urlPath;
        this.requestMethod = requestMethod;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.activity = activity;
        this.loadDialogMessage = loadDialogMessage;
        this.delegate = delegate;
    }

    public RestHelper(String urlPath, HttpMethod requestMethod, HttpHeaders requestHeader, Activity activity, String loadDialogMessage, RestTaskDelegate delegate) {
        this.urlPath = urlPath;
        this.requestMethod = requestMethod;
        this.requestHeader = requestHeader;
        this.requestBody = null;
        this.activity = activity;
        this.loadDialogMessage = loadDialogMessage;
        this.delegate = delegate;
    }

    private void setStatus(String statusText){
        try{
            status = new StatusEntity(statusText);
        }
        catch (Exception e){

        }
    }

    public StatusEntity getStatus(){
        return status;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public void runTask() {
        if(NetworkStateManager.isNetworkAvailable(activity)){
            RestTask restTask = new RestTask(activity, this);
            restTask.execute();
        }
        else{
            View view = activity.findViewById(android.R.id.content);
            Snackbar.make(view, activity.getResources().getString(R.string.enable_network_text), Snackbar.LENGTH_LONG).setAction(activity.getResources().getString(R.string.settings_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    activity.startActivity(intent);
                }
            }).show();
        }
    }

    private void saveToken(TokenEntity token){
        SharedPreferencesManager prefManager = new SharedPreferencesManager(activity);
        prefManager.setKeyValueString(MainConstants.PREFERENCE_TOKEN, token.getToken());
        prefManager.setKeyValueString(MainConstants.PREFERENCE_USERID, token.getUserId());
    }

    @Override
    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
        responseEntity = result;
        if(result.getStatusCode() == HttpStatus.UNAUTHORIZED){
            LoginHelper loginHelper = new LoginHelper(activity, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    if(result.getStatusCode() == HttpStatus.OK) {
                        String stoken = result.getBody();
                        JSONObject jtoken = new JSONObject(stoken);
                        TokenEntity token = new TokenEntity(jtoken);
                        saveToken(token);
                        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(activity);
                        HttpHeaders header = new HttpHeaders();
                        header.setContentType(MediaType.APPLICATION_JSON);
                        header.set(REQUEST_HEADER_AUTHORIZATION, String.format(REQUEST_HEADER_BEARER, sharedPreferencesManager.getPreferenceString(PREFERENCE_TOKEN)));
                        requestHeader = header;
                        runTask();
                    }
                    else{

                    }
                }
            });
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(activity);
            loginHelper.loginServer(activity.getResources().getString(R.string.login_renew_token), sharedPreferencesManager.getPreferenceString(MainConstants.PREFERENCE_USERNAME), sharedPreferencesManager.getPreferenceString(MainConstants.PREFERENCE_PASSWORD));
        }
        else{
            delegate.TaskCompletionResult(result);
        }
    }

    private class RestTask extends AsyncTask<Void, ResponseEntity<String>, ResponseEntity<String>> {

        private ProgressDialog dialog;
        private Context context;
        private RestTaskDelegate delegate;

        public RestTask(Activity activity, RestTaskDelegate delegate) {
            context = activity;
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
//            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    cancel(true);
//                }
//            });
            this.delegate = delegate;
        }

        protected void onPreExecute() {
            this.dialog.setMessage(loadDialogMessage);
            this.dialog.show();
        }

        @Override
        protected ResponseEntity<String> doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpEntity<String> entity;
                if(requestBody != null) {
                    entity = new HttpEntity<>(requestBody.toString(), requestHeader);
                }
                else {
                    entity = new HttpEntity<>(requestHeader);
                }
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                responseEntity = restTemplate.exchange(urlPath, requestMethod, entity, String.class);
                return responseEntity;
            } catch (HttpStatusCodeException e) {
                RestHelper.this.setStatus(e.getResponseBodyAsString());
                responseEntity = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
                setStatus(e.getResponseBodyAsString());
                return responseEntity;
            }
            catch (RestClientException e){
                dialog.dismiss();
                this.cancel(true);
                return responseEntity;
            }
            catch (Exception e){
                dialog.dismiss();
                this.cancel(true);
                return responseEntity;
            }
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> entity) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                delegate.TaskCompletionResult(entity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            Toast t = Toast.makeText(context, context.getResources().getString(R.string.rest_server_unavailable), Toast.LENGTH_LONG);
            t.show();
        }
    }
}
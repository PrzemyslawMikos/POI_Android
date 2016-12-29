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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import additional.NetworkStateManager;
import additional.RestTaskDelegate;

/**
 * Created by Przemek on 04.12.2016.
 */

public class RestHelper implements RestTaskDelegate {

    private String urlPath;
    private HttpMethod requestMethod;
    private HttpHeaders requestHeader;
    private JSONObject requestBody;
    private Activity activity;
    private String loadDialogMessage;
    private ResponseEntity<String> responseEntity;
    private Boolean result;
    private RestTaskDelegate delegate;
    private HttpClientErrorException exception;

    public RestHelper(String urlPath, HttpMethod requestMethod, HttpHeaders requestHeader, JSONObject requestBody, Activity activity, String loadDialogMessage, RestTaskDelegate delegate) {
        this.urlPath = urlPath;
        this.requestMethod = requestMethod;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.activity = activity;
        this.loadDialogMessage = loadDialogMessage;
        this.result = false;
        this.delegate = delegate;
    }

    public RestHelper(String urlPath, HttpMethod requestMethod, HttpHeaders requestHeader, Activity activity, String loadDialogMessage, RestTaskDelegate delegate) {
        this.urlPath = urlPath;
        this.requestMethod = requestMethod;
        this.requestHeader = requestHeader;
        this.requestBody = null;
        this.activity = activity;
        this.loadDialogMessage = loadDialogMessage;
        this.result = false;
        this.delegate = delegate;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public Boolean getResult() {
        return result;
    }

    public HttpClientErrorException getException() {
        return exception;
    }

    public void runTask() {
        NetworkStateManager networkState = new NetworkStateManager();
        if(networkState.isNetworkAvailable(activity)){
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

    @Override
    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
        responseEntity = result;
        delegate.TaskCompletionResult(result);
    }

    private class RestTask extends AsyncTask<Void, ResponseEntity<String>, ResponseEntity<String>> {

        private ProgressDialog dialog;
        private Context context;
        private RestTaskDelegate delegate;

        public RestTask(Activity activity, RestTaskDelegate delegate) {
            context = activity;
            dialog = new ProgressDialog(context);
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
                // Żądanie GET nie posiada zawartości
                if(requestMethod != HttpMethod.GET) {
                    entity = new HttpEntity<>(requestBody.toString(), requestHeader);
                }
                else {
                    entity = new HttpEntity<>(requestHeader);
                }
                responseEntity = restTemplate.exchange(urlPath, requestMethod, entity, String.class);
                result = true;
                return responseEntity;
            } catch (HttpClientErrorException e) {
                exception = e;
                result = false;
            }
            catch (Exception e){
                result = false;
            }
            result = false;
            return null;
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
    }
}
package rest;

import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.charset.Charset;
import java.util.Arrays;
import constants.RestConstants;
import delegates.RestTaskDelegate;

/**
 * Created by Przemysław Mikos on 05.01.2017.
 */

public class LoginHelper implements RestConstants{

    private Activity activity;
    private RestTaskDelegate delegate;
    private RestHelper restHelper;

    public LoginHelper(Activity activity, RestTaskDelegate delegate) {
        this.activity = activity;
        this.delegate = delegate;
    }

    public RestHelper getRestHelper(){
        return restHelper;
    }

    public void loginServer(String message, String login, String password, boolean dialogCancelable){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
            JSONObject request = new JSONObject();
            request.put(JSON_USERNAME_KEY, login);
            request.put(JSON_PASSWORD_KEY, password);
            restHelper = new RestHelper(REST_TOKEN_POST, HttpMethod.POST, headers, request, activity, message, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    delegate.TaskCompletionResult(result);
                }
            }, dialogCancelable);
            restHelper.runTask();
        }
        catch (Exception e){

        }
    }
}
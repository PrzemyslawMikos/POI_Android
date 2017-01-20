package rest;

import android.app.Activity;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.charset.Charset;
import java.util.Arrays;
import constants.RestConstants;
import delegates.RestTaskDelegate;
import entity.UserEntity;

/**
 * Created by Przemysław Mikos on 05.01.2017.
 */

public class RegisterHelper implements RestConstants{

    private Activity activity;
    private RestTaskDelegate delegate;
    private RestHelper restHelper;

    public RegisterHelper(Activity activity, RestTaskDelegate delegate) {
        this.activity = activity;
        this.delegate = delegate;
    }

    public RestHelper getRestHelper(){
        return restHelper;
    }

    public void registerServer(String message, UserEntity userEntity){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
            restHelper = new RestHelper(REST_REGISTER_POST, HttpMethod.POST, headers, userEntity.toJSON(), activity, message, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    delegate.TaskCompletionResult(result);
                }
            });
            restHelper.runTask();
        }
        catch (Exception e){

        }
    }
}
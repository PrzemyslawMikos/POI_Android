package com.adventure.poi.poi_android;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import delegates.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.StatusEntity;
import entity.UserEntity;
import rest.RestHelper;

public class RegisterActivity extends AppCompatActivity implements RestConstants, MainConstants, RestTaskDelegate {

    private RestHelper restHelper;
    private EditText editNickname, editEmail, editPhone, editUsername, editPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        assignControls();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void assignControls(){
        editNickname = (EditText) findViewById(R.id.editTextNickname);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPhone = (EditText) findViewById(R.id.editTextPhone);
        editUsername = (EditText) findViewById(R.id.editTextUsername);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void register(){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            UserEntity userEntity = new UserEntity(editNickname.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString(), editUsername.getText().toString(), editPassword.getText().toString());
            restHelper = new RestHelper(REST_REGISTER_POST, HttpMethod.POST, headers, userEntity.toJSON(), RegisterActivity.this, getResources().getString(R.string.register_dialog_text), RegisterActivity.this);
            restHelper.runTask();
        }
        catch (Exception e){
            Snackbar.make(findViewById(R.id.activity_register), R.string.server_exception, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
        if(restHelper.getResult()){
            Log.d("asdad", "asdasdasd");
            if(restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK){
                Log.d("asdad", "asdasdasd");
                String sstatus = restHelper.getResponseEntity().getBody();
                JSONObject jstatus = new JSONObject(sstatus);
                StatusEntity status = new StatusEntity(jstatus);
            }
            else{
                Log.d("2", "2");
                checkStatus();
            }
        }else{
            if(restHelper.getResponseEntity().getStatusCode() != HttpStatus.OK){
                Log.d("1", "1");
                checkStatus();
            }
        }
    }

    private void checkStatus(){
        switch (restHelper.getStatus().getStatus()){
            case STATUS_USERNAME_EXIST:
                Snackbar.make(findViewById(R.id.activity_register), R.string.register_username_exist, Snackbar.LENGTH_LONG).show();
                break;
            case STATUS_EMAIL_EXIST:
                Snackbar.make(findViewById(R.id.activity_register), R.string.register_email_exist, Snackbar.LENGTH_LONG).show();
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                Snackbar.make(findViewById(R.id.activity_register), R.string.server_exception, Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
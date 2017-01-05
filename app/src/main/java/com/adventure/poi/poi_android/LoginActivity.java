package com.adventure.poi.poi_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import additional.SharedPreferencesManager;
import delegates.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.TokenEntity;
import rest.RestHelper;

public class LoginActivity extends AppCompatActivity implements RestConstants, MainConstants {

    private RestHelper restHelper;
    private EditText editLogin, editPassword;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignControls();
        checkPermissions();
        loginIfRemember();
    }

    public void assignControls(){
        editLogin = (EditText) findViewById(R.id.editTextLogin);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
    }

    public void onLoginClick(View v){
        login();
    }

    public void onRegisterClick(View v){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    private void login(){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject request = new JSONObject();
            request.put(JSON_USERNAME_KEY, editLogin.getText().toString());
            request.put(JSON_PASSWORD_KEY, editPassword.getText().toString());
            restHelper = new RestHelper(REST_TOKEN_POST, HttpMethod.POST, headers, request, LoginActivity.this, getResources().getString(R.string.login_dialog_text), new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    if(checkData(result)) {
                        Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                        LoginActivity.this.finish();
                        startActivity(intent);
                    }
                    else
                        Snackbar.make(findViewById(R.id.activity_login), R.string.login_snackbar_bad_credentials, Snackbar.LENGTH_LONG).show();
                }
            });
            restHelper.runTask();
        }
        catch(Exception e){
            Snackbar.make(findViewById(R.id.activity_login), R.string.server_exception, Snackbar.LENGTH_LONG).show();
        }
    }

    private void loginIfRemember(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        if(sharedPreferencesManager.hasCredentials()){
            login();
        }
    }

    private void checkPermissions(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION);
            }
        }
    }

    private void saveCredentials(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.setCredentials(editLogin.getText().toString(), editPassword.getText().toString());
    }

    private void saveToken(TokenEntity token){
        SharedPreferencesManager prefManager = new SharedPreferencesManager(LoginActivity.this);
        prefManager.setKeyValue(MainConstants.PREFERENCE_TOKEN, token.getToken());
        prefManager.setKeyValue(MainConstants.PREFERENCE_USERID, token.getUserId());
    }

    public boolean checkData(ResponseEntity<String> result) {
        try{
            if(restHelper.getResult()){
                if(result.getStatusCode() == HttpStatus.OK){
                    String stoken = result.getBody();
                    JSONObject jtoken = new JSONObject(stoken);
                    TokenEntity token = new TokenEntity(jtoken);
                    saveToken(token);
                    if(checkBoxRememberMe.isChecked()){
                        saveCredentials();
                    }
                    return true;
                }
            }else{
                switch (restHelper.getStatus().getStatus()){
                    case STATUS_BAD_CREDENTIALS:
                        Snackbar.make(findViewById(R.id.activity_login), R.string.login_snackbar_bad_credentials, Snackbar.LENGTH_LONG).show();
                        break;
                    case STATUS_WRONG_PARAMS:
                        Snackbar.make(findViewById(R.id.activity_login), R.string.login_bad_data, Snackbar.LENGTH_LONG).show();
                        break;
                    case STATUS_INTERNAL_SERVER_ERROR:
                        Snackbar.make(findViewById(R.id.activity_login), R.string.server_exception, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
            return false;
        }
        catch(Exception e){
            return false;
        }
    }
}
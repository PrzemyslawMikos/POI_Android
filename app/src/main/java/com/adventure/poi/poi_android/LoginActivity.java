package com.adventure.poi.poi_android;

import android.content.Intent;
import android.support.design.widget.Snackbar;
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
import additional.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.TokenEntity;
import entity.UserEntity;
import rest.RestHelper;
import rest.UsersHelper;

public class LoginActivity extends AppCompatActivity implements RestConstants, MainConstants {

    private RestHelper restHelper;
    private EditText editLogin, editPassword;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editLogin = (EditText) findViewById(R.id.editTextLogin);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
    }

    public void onLoginClick(View v){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject request = new JSONObject();
        try {
            request.put(JSON_USERNAME_KEY, editLogin.getText().toString());
            request.put(JSON_PASSWORD_KEY, editPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        restHelper = new RestHelper(REST_TOKEN_POST, HttpMethod.POST, headers, request, LoginActivity.this, getResources().getString(R.string.login_dialog_text), new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                if(CheckData(result)) {
                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }
                else
                    Snackbar.make(findViewById(R.id.activity_login), R.string.login_snackbar_bad_credentials, Snackbar.LENGTH_LONG).show();
            }
        });
        restHelper.runTask();

    }

    private void SaveCredentials(){

    }

    public boolean CheckData(ResponseEntity<String> result) throws JSONException {
        if(restHelper.getResult()){
            if(restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK){
                String stoken = restHelper.getResponseEntity().getBody();
                JSONObject jtoken = new JSONObject(stoken);
                TokenEntity token = new TokenEntity(jtoken);
                SharedPreferencesManager prefManager = new SharedPreferencesManager(LoginActivity.this);
                prefManager.setKeyValue(MainConstants.PREFERENCE_TOKEN, token.getToken());
                prefManager.setKeyValue(MainConstants.PREFERENCE_USERID, token.getUserId());
                if(checkBoxRememberMe.isChecked()){
                    SaveCredentials();
                }
                return true;
            }
        }else{
            return false;
        }
        return false;
    }
}
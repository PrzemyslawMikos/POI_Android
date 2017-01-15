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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import additional.SharedPreferencesManager;
import delegates.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.StatusEntity;
import entity.TokenEntity;
import rest.LoginHelper;
//TODO walidacja inputów
public class LoginActivity extends AppCompatActivity implements RestConstants, MainConstants {

    private LoginHelper loginHelper;
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
        login(editLogin.getText().toString(), editPassword.getText().toString());
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

    private void login(String login, String password){
       loginHelper = new LoginHelper(LoginActivity.this, new RestTaskDelegate() {
           @Override
           public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
               if(result.getStatusCode() == HttpStatus.OK){
                   String stoken = result.getBody();
                   JSONObject jtoken = new JSONObject(stoken);
                   TokenEntity token = new TokenEntity(jtoken);
                   saveToken(token);
                   saveCredentials();
                   // TODO zmienić po testach
                   //Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                   Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                   LoginActivity.this.finish();
                   startActivity(intent);
               }
               else{
                   showMessages(loginHelper.getRestHelper().getStatus());
               }
           }
       });
        loginHelper.loginServer(getResources().getString(R.string.login_dialog_text), login, password);
    }

    private void loginIfRemember(){
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        if(sharedPreferencesManager.getPreferenceBoolean(MainConstants.PREFERENCE_REMEMBER_ME)){
            login(sharedPreferencesManager.getPreferenceString(PREFERENCE_USERNAME), sharedPreferencesManager.getPreferenceString(PREFERENCE_PASSWORD));
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
        sharedPreferencesManager.setKeyValueBoolean(MainConstants.PREFERENCE_REMEMBER_ME, checkBoxRememberMe.isChecked());
    }

    private void saveToken(TokenEntity token){
        SharedPreferencesManager prefManager = new SharedPreferencesManager(LoginActivity.this);
        prefManager.setKeyValueString(MainConstants.PREFERENCE_TOKEN, token.getToken());
        prefManager.setKeyValueString(MainConstants.PREFERENCE_USERID, token.getUserId());
    }

    private void showMessages(StatusEntity entity) {
        switch (entity.getStatus()){
            case STATUS_BAD_CREDENTIALS:
                Snackbar.make(findViewById(R.id.activity_login), R.string.login_snackbar_bad_credentials, Snackbar.LENGTH_LONG).show();
                break;
            case STATUS_WRONG_PARAMS:
                Snackbar.make(findViewById(R.id.activity_login), R.string.login_bad_data, Snackbar.LENGTH_LONG).show();
                break;
            case STATUS_USER_BLOCKED:
                Snackbar.make(findViewById(R.id.activity_login), R.string.login_user_blocked, Snackbar.LENGTH_LONG).show();
                break;
            case STATUS_INTERNAL_SERVER_ERROR:
                Snackbar.make(findViewById(R.id.activity_login), R.string.server_exception, Snackbar.LENGTH_LONG).show();
                break;
        }
    }
}
package com.adventure.poi.poi_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import additional.TaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.StatusEntity;
import entity.UserEntity;
import rest.RestHelper;

public class RegisterActivity extends AppCompatActivity implements RestConstants, TaskDelegate, MainConstants {

    private RestHelper restHelper;
    private EditText editNickname, editEmail, editPhone, editUsername, editPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editNickname = (EditText) findViewById(R.id.editTextNickname);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPhone = (EditText) findViewById(R.id.editTextPhone);
        editUsername = (EditText) findViewById(R.id.editTextUsername);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        btnRegister = (Button) findViewById(R.id.buttonRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }

    private void Register(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UserEntity userEntity = new UserEntity(editNickname.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString(), editUsername.getText().toString(), editPassword.getText().toString());
        restHelper = new RestHelper(REST_REGISTER_POST, HttpMethod.POST, headers, userEntity.toJSON(), RegisterActivity.this, getResources().getString(R.string.register_dialog_text), RegisterActivity.this);
        restHelper.runTask();
    }

    @Override
    public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
        if(restHelper.getResult()){
            if(restHelper.getResponseEntity().getStatusCode() == HttpStatus.OK){
                String sstatus = restHelper.getResponseEntity().getBody();
                JSONObject jstatus = new JSONObject(sstatus);
                StatusEntity status = new StatusEntity(jstatus);
                Toast.makeText(RegisterActivity.this, "zarejestrowano, status: " + status.getStatus(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(RegisterActivity.this, "Wyjatek: " + restHelper.getException().getStatusCode().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
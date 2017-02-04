package com.adventure.poi.poi_android;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import additional.InputValidation;
import delegates.RestTaskDelegate;
import constants.MainConstants;
import constants.RestConstants;
import entity.StatusEntity;
import entity.UserEntity;
import rest.RegisterHelper;

public class RegisterActivity extends AppCompatActivity implements RestConstants, MainConstants {

    private RegisterHelper registerHelper;
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
        if(InputValidation.validateNickname(editNickname) && InputValidation.validateEmail(editEmail) && InputValidation.validatePhone(editPhone) && InputValidation.validteUsername(editUsername) && InputValidation.validatePassword(editPassword)){
            UserEntity userEntity = new UserEntity(editNickname.getText().toString(), editEmail.getText().toString(), editPhone.getText().toString(), editUsername.getText().toString(), editPassword.getText().toString());
            registerHelper = new RegisterHelper(RegisterActivity.this, new RestTaskDelegate() {
                @Override
                public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                    if(result.getStatusCode() == HttpStatus.OK){
                        Toast t = Toast.makeText(getApplicationContext(), R.string.register_complete, Toast.LENGTH_LONG);
                        t.show();
                        RegisterActivity.this.finish();
                    }
                    else{
                        showMessages(registerHelper.getRestHelper().getStatus());
                    }
                }
            });
            registerHelper.registerServer(getResources().getString(R.string.register_dialog_text), userEntity, true);
        }
    }

    private void showMessages(StatusEntity entity){
        switch (entity.getStatus()){
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
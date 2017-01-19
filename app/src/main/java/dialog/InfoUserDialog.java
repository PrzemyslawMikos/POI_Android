package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;
import com.adventure.poi.poi_android.R;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import additional.SharedPreferencesManager;
import constants.MainConstants;
import delegates.RestTaskDelegate;
import entity.UserEntity;
import rest.UsersHelper;

/**
 * Created by Przemysław Mikos on 19.01.2017.
 */

public class InfoUserDialog implements MainConstants {

    private Dialog userDialog;
    private Activity activity;
    private UserEntity user;
    private UsersHelper usersHelper;

    public InfoUserDialog(Activity activity) {
        this.activity = activity;
    }

    public void show(){
        userDialog = new Dialog(activity);
        userDialog.setContentView(R.layout.info_user_dialog);
        userDialog.setCancelable(true);
        usersHelper = new UsersHelper(activity, new RestTaskDelegate() {
            @Override
            public void TaskCompletionResult(ResponseEntity<String> result) throws JSONException {
                user = usersHelper.getUsers().get(0);
                TextView textViewUserInfo = (TextView) userDialog.findViewById(R.id.textViewUserInfo);
                textViewUserInfo.setText("Nick: " + user.getNickname() + "\nNazwa użytkownika: " + user.getUsername() + "\nEmail: " + user.getEmail() + "\nTelefon: " + user.getPhone() + "\nData rejestracji: " + user.getCreationdate());
                userDialog.show();
            }
        });
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(activity);
        usersHelper.getUserById(activity.getResources().getString(R.string.downloading_user_data), sharedPreferencesManager.getPreferenceString(MainConstants.PREFERENCE_USERID));

    }
}
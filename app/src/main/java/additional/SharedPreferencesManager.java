package additional;

import android.content.Context;
import android.content.SharedPreferences;
import constants.MainConstants;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Przemysław Mikos on 12.12.2016.
 */
// TODO hasło nie może być zapisane jako czysty tekst !!
public class SharedPreferencesManager implements MainConstants{

    private Context context;

    public SharedPreferencesManager(Context context){
        this.context = context;
    }

    public boolean hasCredentials(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        if(sharedPreferences.contains(PREFERENCE_USERNAME) && sharedPreferences.contains(PREFERENCE_PASSWORD)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setCredentials(String username, String password){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit()
                    .putString(PREFERENCE_USERNAME, username)
                    .putString(PREFERENCE_PASSWORD, password)
                    .apply();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean setKeyValue(String key, String value){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit()
                    .putString(key, value)
                    .apply();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean unsetCredentials(){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit()
                    .remove(PREFERENCE_USERNAME)
                    .remove(PREFERENCE_PASSWORD)
                    .apply();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean unsetKey(String key){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences();
            sharedPreferences.edit()
                    .remove(key)
                    .apply();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public String getPreference(String preference){
        try{
            SharedPreferences sharedPreferences = getSharedPreferences();
            if(sharedPreferences != null){
                String valuePreference;
                valuePreference = sharedPreferences.getString(preference, "");
                return valuePreference;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    private SharedPreferences getSharedPreferences(){
        try {
            return context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        }
        catch(Exception e){
            return null;
        }
    }
}
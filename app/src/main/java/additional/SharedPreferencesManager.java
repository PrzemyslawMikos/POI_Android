package additional;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import constants.MainConstants;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Przemek on 12.12.2016.
 */
// TODO hasło i login nie może być zapisane jako czysty tekst !!
public class SharedPreferencesManager implements MainConstants{

    private Context context;

    public SharedPreferencesManager(Context context){
        this.context = context;
    }

    public boolean setCredentials(String username, String password){
        try{
            SharedPreferences SPref = getSharedPreferences();
            SPref.edit()
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
            SharedPreferences SPref = getSharedPreferences();
            SPref.edit()
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
            SharedPreferences SPref = getSharedPreferences();
            SPref.edit()
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
            SharedPreferences SPref = getSharedPreferences();
            SPref.edit()
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
            SharedPreferences SPref = getSharedPreferences();
            if(SPref != null){
                String valuePreference;
                valuePreference = SPref.getString(preference, "");
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

    public String getTextHash(String text){
        try{
            return this.hashSHA256(text);
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

    //TODO do zmiany!!
    private String hashSHA256(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return String.format("%064x", new java.math.BigInteger(1, digest));
    }

}
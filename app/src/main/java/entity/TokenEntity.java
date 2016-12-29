package entity;

import org.json.JSONException;
import org.json.JSONObject;
import constants.RestConstants;

/**
 * Created by Przemek on 04.12.2016.
 */

public class TokenEntity implements RestConstants {

    private String token;
    private String userid;

    public String getToken() {
        return token;
    }

    public String getUserId(){
        return userid;
    }

    public TokenEntity(JSONObject jToken) throws JSONException {
        this.token = jToken.getString(JSON_TOKEN_KEY);
        this.userid = jToken.getString(JSON_USERID_KEY);
    }
}
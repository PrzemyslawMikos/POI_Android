package entity;

import org.json.JSONException;
import org.json.JSONObject;
import constants.RestConstants;

/**
 * Created by Przemek on 04.12.2016.
 */

public class TokenEntity implements RestConstants {

    private String token;

    public String getToken() {
        return token;
    }

    public TokenEntity(JSONObject jToken) throws JSONException {
        this.token = jToken.getString(JSON_TOKEN_KEY);
    }
}
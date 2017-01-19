package entity;

import org.json.JSONException;
import org.json.JSONObject;
import constants.RestConstants;

/**
 * Created by Przemysław Mikos on 12.12.2016.
 */

public class StatusEntity implements RestConstants{

    private String status;

    public String getStatus() {
        return status;
    }

    public StatusEntity(String sStatus) throws JSONException {
        JSONObject jStatus = new JSONObject(sStatus);
        this.status = jStatus.getString(JSON_STATUS_KEY);
    }

}
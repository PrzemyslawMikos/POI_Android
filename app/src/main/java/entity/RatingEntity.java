package entity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import constants.RestConstants;

/**
 * Created by Przemysław Mikos on 12.01.2017.
 */

public class RatingEntity implements RestConstants, Serializable {

    private int rating;
    private long pointid;
    private long userid;

    public RatingEntity(int rating, long pointid, long userid) {
        this.rating = rating;
        this.pointid = pointid;
        this.userid = userid;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate(JSON_RATING_KEY, this.rating);
            jsonObject.accumulate(JSON_POINTID_KEY, this.pointid);
            jsonObject.accumulate(JSON_USERID_KEY, this.userid);
            return jsonObject;
        }catch(JSONException e){
            return null;
        }
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getPointid() {
        return pointid;
    }

    public void setPointid(long pointid) {
        this.pointid = pointid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

}
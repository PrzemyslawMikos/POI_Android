package entity;

import org.json.JSONException;
import org.json.JSONObject;
import constants.RestConstants;

/**
 * Created by Przemek on 12.12.2016.
 */

public class PointEntity implements RestConstants{

    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
    private String description;
    private String picture;
    private String mimetype;
    private String addeddate;
    private Integer typeid;
    private Long userid;

    public PointEntity(Double longitude, Double latitude, String name, String description, String picture, String mimetype, Integer typeid, Long userid) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.mimetype = mimetype;
        this.typeid = typeid;
        this.userid = userid;
    }

    public PointEntity(JSONObject jPoint) throws JSONException {
        this.id = jPoint.getLong(JSON_ID_KEY);
        this.longitude = jPoint.getDouble(JSON_LONGITUDE_KEY);
        this.latitude = jPoint.getDouble(JSON_LATITUDE_KEY);
        this.name = jPoint.getString(JSON_NAME_KEY);
        this.description = jPoint.getString(JSON_DESCRIPTION_KEY);
        this.picture = jPoint.getString(JSON_PICTURE_KEY);
        this.mimetype = jPoint.getString(JSON_MIMETYPE_KEY);
        this.addeddate = jPoint.getString(JSON_ADDEDDATE_KEY);
        this.typeid= jPoint.getInt(JSON_TYPEID_KEY);
        this.userid = jPoint.getLong(JSON_USERID_KEY);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate(JSON_LONGITUDE_KEY, this.longitude.toString());
            jsonObject.accumulate(JSON_LATITUDE_KEY, this.latitude.toString());
            jsonObject.accumulate(JSON_NAME_KEY, this.name);
            jsonObject.accumulate(JSON_DESCRIPTION_KEY, this.description);
            jsonObject.accumulate(JSON_PICTURE_KEY, this.picture);
            jsonObject.accumulate(JSON_MIMETYPE_KEY, this.mimetype);
            jsonObject.accumulate(JSON_TYPEID_KEY, this.typeid.toString());
            jsonObject.accumulate(JSON_USERID_KEY, this.userid.toString());
            return jsonObject;
        }catch(JSONException e){
            return null;
        }
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(String addeddate) {
        this.addeddate = addeddate;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

}
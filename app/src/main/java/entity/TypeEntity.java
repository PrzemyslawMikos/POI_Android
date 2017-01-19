package entity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import constants.RestConstants;

/**
 * Created by Przemysław Mikos on 12.12.2016.
 */

public class TypeEntity implements RestConstants, Serializable{

    private int id;
    private String name;
    private String description;
    private String addeddate;
    private String image;
    private String mimetype;

    public TypeEntity(String name, String description, String addeddate, String image, String mimetype) {
        this.name = name;
        this.description = description;
        this.addeddate = addeddate;
        this.image = image;
        this.mimetype = mimetype;
    }

    public TypeEntity(JSONObject jType) throws JSONException {
        this.id = jType.getInt(JSON_ID_KEY);
        this.name = jType.getString(JSON_NAME_KEY);
        this.description = jType.getString(JSON_DESCRIPTION_KEY);
        this.addeddate = jType.getString(JSON_ADDEDDATE_KEY);
        this.image = jType.getString(JSON_IMAGE_KEY);
        this.mimetype = jType.getString(JSON_MIMETYPE_KEY);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(String addeddate) {
        this.addeddate = addeddate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}
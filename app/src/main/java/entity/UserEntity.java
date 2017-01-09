package entity;

import org.json.JSONException;
import org.json.JSONObject;
import constants.RestConstants;

/**
 * Created by Przemek on 12.12.2016.
 */

public class UserEntity implements RestConstants {

    private Integer id;
    private Integer permissionid;
    private String nickname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String creationdate;
    private Boolean firstlogin;
    private Boolean unblocked;


    public UserEntity(JSONObject jUser) throws JSONException {
        this.id = jUser.getInt(JSON_ID_KEY);
        this.permissionid = jUser.getInt(JSON_PERMISSIONID_KEY);
        this.nickname = jUser.getString(JSON_NICKNAME_KEY);
        this.email = jUser.getString(JSON_EMAIL_KEY);
        this.phone = jUser.getString(JSON_PHONE_KEY);
        this.username = jUser.getString(JSON_USERNAME_KEY);
        this.creationdate = jUser.getString(JSON_CREATIONDATE_KEY);
        this.firstlogin = jUser.getBoolean(JSON_FIRSTLOGIN_KEY);
        this.unblocked = jUser.getBoolean(JSON_UNBLOCKED_KEY);
    }

    public UserEntity(String nickname, String email, String phone, String username, String password) {
        this.id = null;
        this.permissionid = null;
        this.creationdate = null;
        this.firstlogin = null;
        this.unblocked = null;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.accumulate(JSON_NICKNAME_KEY, this.nickname);
            jsonObject.accumulate(JSON_EMAIL_KEY, this.email);
            jsonObject.accumulate(JSON_PHONE_KEY, this.phone);
            jsonObject.accumulate(JSON_USERNAME_KEY, this.username);
            jsonObject.accumulate(JSON_PASSWORD_KEY, this.password);
            return jsonObject;
        }catch(JSONException e){
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPermissionid() {
        return permissionid;
    }

    public void setPermissionid(Integer permissionid) {
        this.permissionid = permissionid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public Boolean getFirstlogin() {
        return firstlogin;
    }

    public void setFirstlogin(Boolean firstlogin) {
        this.firstlogin = firstlogin;
    }

    public Boolean getUnblocked() {
        return unblocked;
    }

    public void setUnblocked(Boolean unblocked) {
        this.unblocked = unblocked;
    }
}
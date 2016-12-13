package entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Przemek on 12.12.2016.
 */

public class UserEntity {

    private Integer id;
    private String nickname;
    private String email;
    private String phone;
    private String username;
    private String password;
    private Date creationdate;
    private Boolean firstlogin;
    private Boolean unblocked;

    public UserEntity(String nickname, String email, String phone, String username, String password) {
        this.id = null;
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
            jsonObject.accumulate("nickname", this.nickname);
            jsonObject.accumulate("email", this.email);
            jsonObject.accumulate("phone", this.phone);
            jsonObject.accumulate("username", this.username);
            jsonObject.accumulate("password", this.password);
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

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
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

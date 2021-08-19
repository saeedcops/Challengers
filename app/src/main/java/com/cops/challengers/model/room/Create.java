
package com.cops.challengers.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Create {


    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("facebook_name")
    @Expose
    private String facebookName;
    @SerializedName("facebook_image")
    @Expose
    private String facebookImage;
    @SerializedName("facebook_id")
    @Expose
    private String facebookId;
    @SerializedName("friend")
    @Expose
    private String [] friend;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getFacebookImage() {
        return facebookImage;
    }

    public void setFacebookImage(String facebookImage) {
        this.facebookImage = facebookImage;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String [] getFriend() {
        return friend;
    }

    public void setFriend(String [] friend) {
        this.friend = friend;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}

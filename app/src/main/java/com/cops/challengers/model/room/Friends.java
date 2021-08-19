
package com.cops.challengers.model.room;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friends {

    @SerializedName("friends")
    @Expose
    private List<Profile> friends = null;

    public List<Profile> getFriends() {
        return friends;
    }

    public void setFriends(List<Profile> friends) {
        this.friends = friends;
    }

}

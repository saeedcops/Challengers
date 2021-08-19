
package com.cops.challengers.model.room;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("first_prize")
    @Expose
    private Integer firstPrize;
    @SerializedName("scond_prize")
    @Expose
    private Integer scondPrize;
    @SerializedName("rest_prize")
    @Expose
    private Integer restPrize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getFirstPrize() {
        return firstPrize;
    }

    public void setFirstPrize(Integer firstPrize) {
        this.firstPrize = firstPrize;
    }

    public Integer getScondPrize() {
        return scondPrize;
    }

    public void setScondPrize(Integer scondPrize) {
        this.scondPrize = scondPrize;
    }

    public Integer getRestPrize() {
        return restPrize;
    }

    public void setRestPrize(Integer restPrize) {
        this.restPrize = restPrize;
    }

}

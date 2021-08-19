
package com.cops.challengers.model.room;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("level")
    @Expose
    private Integer level;

    @SerializedName("next_level")
    @Expose
    private Integer nextLevel;

    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("next_gift")
    @Expose
    private String nextGift;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("coins")
    @Expose
    private Integer coins;
    @SerializedName("gem")
    @Expose
    private Integer gem;
    @SerializedName("league_coins")
    @Expose
    private Integer leagueCoins;

    @SerializedName("win")
    @Expose
    private Integer win;
    @SerializedName("loss")
    @Expose
    private Integer loss;
    @SerializedName("draw")
    @Expose
    private Integer draw;

    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("my_league")
    @Expose
    private Integer myLeague;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("daily_coins")
    @Expose
    private Boolean dailyCoins;
    @SerializedName("league_prize")
    @Expose
    private Boolean leaguePrize;
    @SerializedName("friends")
    @Expose
    private List<Object> friends = null;
    @SerializedName("l_prize")
    @Expose
    private Integer lPrize;
    @SerializedName("l_rank")
    @Expose
    private Integer lRank;
    @SerializedName("l_coins")
    @Expose
    private Integer lCoins;

    @SerializedName("l_league")
    @Expose
    private String lLeague;
    @SerializedName("l_next")
    @Expose
    private String lNext;
    @SerializedName("l_image")
    @Expose
    private String lImage;

    public Integer getlPrize() {
        return lPrize;
    }

    public void setlPrize(Integer lPrize) {
        this.lPrize = lPrize;
    }

    public Integer getlRank() {
        return lRank;
    }

    public void setlRank(Integer lRank) {
        this.lRank = lRank;
    }

    public Integer getlCoins() {
        return lCoins;
    }

    public void setlCoins(Integer lCoins) {
        this.lCoins = lCoins;
    }

    public String getlLeague() {
        return lLeague;
    }

    public void setlLeague(String lLeague) {
        this.lLeague = lLeague;
    }

    public String getlNext() {
        return lNext;
    }

    public void setlNext(String lNext) {
        this.lNext = lNext;
    }

    public String getlImage() {
        return lImage;
    }

    public void setlImage(String lImage) {
        this.lImage = lImage;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    public Boolean getLeaguePrize() {
        return leaguePrize;
    }

    public void setLeaguePrize(Boolean leaguePrize) {
        this.leaguePrize = leaguePrize;
    }

    public Boolean getDailyCoins() {
        return dailyCoins;
    }

    public void setDailyCoins(Boolean dailyCoins) {
        this.dailyCoins = dailyCoins;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNextGift() {
        return nextGift;
    }

    public void setNextGift(String nextGift) {
        this.nextGift = nextGift;
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

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getGem() {
        return gem;
    }

    public void setGem(Integer gem) {
        this.gem = gem;
    }

    public Integer getLeagueCoins() {
        return leagueCoins;
    }

    public void setLeagueCoins(Integer leagueCoins) {
        this.leagueCoins = leagueCoins;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getMyLeague() {
        return myLeague;
    }

    public void setMyLeague(Integer myLeague) {
        this.myLeague = myLeague;
    }

    public List<Object> getFriends() {
        return friends;
    }

    public void setFriends(List<Object> friends) {
        this.friends = friends;
    }

}

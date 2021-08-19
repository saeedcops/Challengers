
package com.cops.challengers.model.room;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;
    @SerializedName("players_answer")
    @Expose
    private List<PlayersAnswer> playersAnswer = null;
    @SerializedName("player1")
    @Expose
    private Profile player1;
    @SerializedName("player2")
    @Expose
    private Profile player2;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("avaliable")
    @Expose
    private Boolean avaliable;
    @SerializedName("player1_prize")
    @Expose
    private Integer player1Prize;
    @SerializedName("player2_prize")
    @Expose
    private Integer player2Prize;
    @SerializedName("player1_profile")
    @Expose
    private Integer player1Profile;
    @SerializedName("player2_profile")
    @Expose
    private Integer player2Profile;
    @SerializedName("question_num")
    @Expose
    private Integer questionNum;
    @SerializedName("player1_can_play")
    @Expose
    private Boolean player1CanPlay;
    @SerializedName("player2_can_play")
    @Expose
    private Boolean player2CanPlay;
    @SerializedName("player1_score")
    @Expose
    private Integer player1Score;
    @SerializedName("player2_score")
    @Expose
    private Integer player2Score;
    @SerializedName("player1_left")
    @Expose
    private Boolean player1Left;
    @SerializedName("player2_left")
    @Expose
    private Boolean player2Left;
    @SerializedName("player1_answered")
    @Expose
    private Integer player1Answered;
    @SerializedName("player2_answered")
    @Expose
    private Integer player2Answered;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("updated")
    @Expose
    private String updated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<PlayersAnswer> getPlayersAnswer() {
        return playersAnswer;
    }

    public void setPlayersAnswer(List<PlayersAnswer> playersAnswer) {
        this.playersAnswer = playersAnswer;
    }

    public Profile getPlayer1() {
        return player1;
    }

    public void setPlayer1(Profile player1) {
        this.player1 = player1;
    }

    public Profile getPlayer2() {
        return player2;
    }

    public void setPlayer2(Profile player2) {
        this.player2 = player2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(Boolean avaliable) {
        this.avaliable = avaliable;
    }

    public Integer getPlayer1Prize() {
        return player1Prize;
    }

    public void setPlayer1Prize(Integer player1Prize) {
        this.player1Prize = player1Prize;
    }

    public Integer getPlayer2Prize() {
        return player2Prize;
    }

    public void setPlayer2Prize(Integer player2Prize) {
        this.player2Prize = player2Prize;
    }

    public Integer getPlayer1Profile() {
        return player1Profile;
    }

    public void setPlayer1Profile(Integer player1Profile) {
        this.player1Profile = player1Profile;
    }

    public Integer getPlayer2Profile() {
        return player2Profile;
    }

    public void setPlayer2Profile(Integer player2Profile) {
        this.player2Profile = player2Profile;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Boolean getPlayer1CanPlay() {
        return player1CanPlay;
    }

    public void setPlayer1CanPlay(Boolean player1CanPlay) {
        this.player1CanPlay = player1CanPlay;
    }

    public Boolean getPlayer2CanPlay() {
        return player2CanPlay;
    }

    public void setPlayer2CanPlay(Boolean player2CanPlay) {
        this.player2CanPlay = player2CanPlay;
    }

    public Integer getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(Integer player1Score) {
        this.player1Score = player1Score;
    }

    public Integer getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(Integer player2Score) {
        this.player2Score = player2Score;
    }

    public Boolean getPlayer1Left() {
        return player1Left;
    }

    public void setPlayer1Left(Boolean player1Left) {
        this.player1Left = player1Left;
    }

    public Boolean getPlayer2Left() {
        return player2Left;
    }

    public void setPlayer2Left(Boolean player2Left) {
        this.player2Left = player2Left;
    }

    public Integer getPlayer1Answered() {
        return player1Answered;
    }

    public void setPlayer1Answered(Integer player1Answered) {
        this.player1Answered = player1Answered;
    }

    public Integer getPlayer2Answered() {
        return player2Answered;
    }

    public void setPlayer2Answered(Integer player2Answered) {
        this.player2Answered = player2Answered;
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

}

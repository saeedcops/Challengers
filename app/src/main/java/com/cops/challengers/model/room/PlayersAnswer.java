
package com.cops.challengers.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayersAnswer {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("room")
    @Expose
    private Integer room;
    @SerializedName("player1_id")
    @Expose
    private Integer player1Id;
    @SerializedName("player2_id")
    @Expose
    private Integer player2Id;
    @SerializedName("player1_score")
    @Expose
    private Integer player1Score;
    @SerializedName("player2_score")
    @Expose
    private Integer player2Score;
    @SerializedName("player1_answer")
    @Expose
    private String player1Answer;
    @SerializedName("player2_answer")
    @Expose
    private String player2Answer;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("correct")
    @Expose
    private String correct;
    @SerializedName("question_num")
    @Expose
    private Integer questionNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Integer player1Id) {
        this.player1Id = player1Id;
    }

    public Integer getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Integer player2Id) {
        this.player2Id = player2Id;
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

    public String getPlayer1Answer() {
        return player1Answer;
    }

    public void setPlayer1Answer(String player1Answer) {
        this.player1Answer = player1Answer;
    }

    public String getPlayer2Answer() {
        return player2Answer;
    }

    public void setPlayer2Answer(String player2Answer) {
        this.player2Answer = player2Answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }
}

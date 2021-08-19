
package com.cops.challengers.model.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("obtion1")
    @Expose
    private String obtion1;
    @SerializedName("obtion2")
    @Expose
    private String obtion2;
    @SerializedName("obtion3")
    @Expose
    private String obtion3;
    @SerializedName("obtion4")
    @Expose
    private String obtion4;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("question_type")
    @Expose
    private Integer questionType;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getObtion1() {
        return obtion1;
    }

    public void setObtion1(String obtion1) {
        this.obtion1 = obtion1;
    }

    public String getObtion2() {
        return obtion2;
    }

    public void setObtion2(String obtion2) {
        this.obtion2 = obtion2;
    }

    public String getObtion3() {
        return obtion3;
    }

    public void setObtion3(String obtion3) {
        this.obtion3 = obtion3;
    }

    public String getObtion4() {
        return obtion4;
    }

    public void setObtion4(String obtion4) {
        this.obtion4 = obtion4;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
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

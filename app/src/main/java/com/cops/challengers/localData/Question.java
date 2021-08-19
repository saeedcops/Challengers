package com.cops.challengers.localData;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question")
public class Question {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private Integer id;
    private String question;
    private String category;
    private String answer;
    private String obtion1;
    private String obtion2;
    private String obtion3;
    private String obtion4;
    private String image;
    private Integer time;
    private Integer questionType;
    private String created;
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

package com.cops.challengers.localData;

import android.app.Application;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


public class Repository {

   private QuestionsDao questionsDao;
   private AnswersDao answersDao;

    public Repository(Application application) {

        QuestionsDataBase dataBase = QuestionsDataBase.getInstance(application);
        questionsDao = dataBase.questionsDao();
        answersDao = dataBase.answersDao();

    }


    public void insertQuestion(List<Question> question){
        questionsDao.insert(question);
    }

    public Single<List<Question>> getQuestion(){

        return questionsDao.getQuestion();
    }


    public Single<List<Answers>> getResult(int result){

        return answersDao.getResult(result);
    }

    public void insertAnswer(Answers answers){

        answersDao.insertAnswer(answers);
    }

    public void deleteAnswers(){

        answersDao.deleteAnswers();
    }

}

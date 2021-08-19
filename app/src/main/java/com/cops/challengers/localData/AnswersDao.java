package com.cops.challengers.localData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


@Dao
public interface AnswersDao {


    @Query("SELECT DISTINCT * FROM answers WHERE result ==:result ")
    Single<List<Answers>> getResult(int result);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnswer(Answers answers);

    @Query("DELETE FROM answers")
    void deleteAnswers();


}

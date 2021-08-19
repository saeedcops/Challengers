package com.cops.challengers.localData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Single;


@Dao
public interface QuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Question> question);

    @Query("SELECT DISTINCT * FROM question ORDER BY random()  ")
    Single<List<Question>> getQuestion();

}

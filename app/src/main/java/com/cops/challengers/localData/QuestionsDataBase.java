package com.cops.challengers.localData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { Question.class,Answers.class},exportSchema = false,version = 2)
public abstract class QuestionsDataBase extends RoomDatabase {

    private static String DB_NAME = "challengers_v1.db";
    private static QuestionsDataBase instance;

    public abstract QuestionsDao questionsDao();
    public abstract AnswersDao answersDao();

    public static synchronized QuestionsDataBase getInstance(Context context){

        if(instance==null){

            instance= Room.databaseBuilder(context.getApplicationContext(),
                    QuestionsDataBase.class,DB_NAME)
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .createFromAsset(DB_NAME)

                    .build();

        }
        return instance;
    }

}

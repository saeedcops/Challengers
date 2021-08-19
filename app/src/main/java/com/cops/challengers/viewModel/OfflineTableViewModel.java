package com.cops.challengers.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.cops.challengers.localData.Answers;
import com.cops.challengers.localData.Question;
import com.cops.challengers.localData.Repository;


import java.util.List;
import io.reactivex.rxjava3.core.Single;


public class OfflineTableViewModel extends AndroidViewModel {


    private Repository repository;

    public OfflineTableViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
    public Single<List<Question>> getQuestions(){

       return repository.getQuestion();
    }



    public void setAnswer(Answers  answer){

        repository.insertAnswer(answer);

    }

    @Override
    protected void onCleared() {

        super.onCleared();

    }


}

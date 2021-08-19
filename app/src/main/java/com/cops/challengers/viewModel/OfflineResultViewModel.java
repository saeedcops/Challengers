package com.cops.challengers.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.cops.challengers.localData.Answers;
import com.cops.challengers.localData.Repository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class OfflineResultViewModel extends AndroidViewModel {


    private Repository repository;

    public OfflineResultViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
    @Override
    protected void onCleared() {
        super.onCleared();

    }
    public Single<List<Answers>> getResult(int resultId){

        return repository.getResult(resultId);

    }


    public void deleteAnswers(){

         repository.deleteAnswers();

    }

}

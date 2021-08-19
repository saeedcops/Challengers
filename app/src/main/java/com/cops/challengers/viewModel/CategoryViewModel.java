package com.cops.challengers.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.localData.Repository;
import com.cops.challengers.model.room.Question;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryViewModel extends AndroidViewModel {


    public MutableLiveData<Integer> questionsMutableLiveData = new MutableLiveData<>();
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void getQuestions(String token) {

        ApiService userService = RetrofitClient.createService(ApiService.class, token);

        userService.getQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                if (response.isSuccessful()) {

                    List<com.cops.challengers.localData.Question> questions1=new ArrayList<>();
                    int sum=1;
                    for (Question q : response.body()) {
                        sum++;
                        com.cops.challengers.localData.Question question = new com.cops.challengers.localData.Question();
                        question.setId(q.getId());
                        question.setAnswer(q.getAnswer());
                        question.setCategory(q.getCategory());
                        question.setCreated(q.getCreated());
                        question.setImage(q.getImage());
                        question.setObtion1(q.getObtion1());
                        question.setObtion2(q.getObtion2());
                        question.setObtion3(q.getObtion3());
                        question.setObtion4(q.getObtion4());
                        question.setQuestion(q.getQuestion());
                        question.setQuestionType(q.getQuestionType());
                        question.setTime(q.getTime());
                        question.setUpdated(q.getUpdated());

                        questions1.add(question);

                        int per=(int)((sum * 100.0f) / response.body().size());
                        questionsMutableLiveData.setValue(per);
//                        Log.i("PSum",sum +" ? "+response.body().size());
                    }

                    insertQuestion(questions1);

                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });

    }

    private void insertQuestion(List<com.cops.challengers.localData.Question> question) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                repository.insertQuestion(question);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                questionsMutableLiveData.setValue(100);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

package com.cops.challengers.view.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cops.challengers.R;
import com.cops.challengers.adapters.ResultAdapter;
import com.cops.challengers.localData.Answers;
import com.cops.challengers.viewModel.OfflineResultViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineResultFragment extends BaseFragment {


    @BindView(R.id.offline_result_fragment_result)
    TextView offlineResultFragmentResult;
    @BindView(R.id.offline_result_fragment_rv)
    RecyclerView offlineResultFragmentRv;
    private NavController navController;
    private OfflineResultViewModel resultViewModel;
    private ResultAdapter resultAdapter;
    private MediaPlayer applauseMedia;
    private int resultId;
    private boolean isHome;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public OfflineResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // SETTING ADS
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        return inflater.inflate(R.layout.fragment_offline_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdView mAdView = view.findViewById(R.id.offline_result_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ButterKnife.bind(this, view);
        setUpActivity();
        resultViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(OfflineResultViewModel.class);
        navController = Navigation.findNavController(view);
        resultId = OfflineResultFragmentArgs.fromBundle(getArguments()).getResultId();
        isHome = OfflineResultFragmentArgs.fromBundle(getArguments()).getHome();


        applauseMedia = MediaPlayer.create(getContext(), R.raw.applause);

        compositeDisposable.add(resultViewModel.getResult(resultId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Answers>>() {
                    @Override
                    public void accept(List<Answers> answers) throws Throwable {

                        if(answers.size()>0) {
                            applauseMedia.start();
                            offlineResultFragmentResult.setText(OfflineResultFragmentArgs.fromBundle(getArguments()).getCorrect()+" / "+answers.size());

                            resultAdapter = new ResultAdapter(answers, ResultFragmentArgs.fromBundle(getArguments()).getQsnLang(), 0);

                            offlineResultFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
                            offlineResultFragmentRv.setAdapter(resultAdapter);

                        }
                    }
                }));



    }


    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        getViewModelStore().clear();
        super.onDestroy();


    }

    private void delete(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                resultViewModel.deleteAnswers();

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                        if(isHome){

                            OfflineResultFragmentDirections.ActionOfflineResultFragmentToHomeFragment action=OfflineResultFragmentDirections.actionOfflineResultFragmentToHomeFragment();
                            navController.navigate(action);
                        }else{

                            navController.popBackStack();

                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void onBack() {
//        super.onBack();
        delete();
    }

    @OnClick(R.id.offline_result_fragment_btn_home)
    public void onViewClicked() {

        delete();
    }
}

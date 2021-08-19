package com.cops.challengers.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;
import com.cops.challengers.adapters.TableWriteAdapter;
import com.cops.challengers.localData.Answers;
import com.cops.challengers.localData.Question;
import com.cops.challengers.view.dialogs.ExitDialog;
import com.cops.challengers.viewModel.OfflineTableViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


//import com.cops.challengers.model.Room;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineTableFragment extends BaseFragment implements TableWriteAdapter.HomeAdapterClick, ExitDialog.OnExitChange {


    @BindView(R.id.offline_table_fragment_tv_question_number)
    TextView offlineTableFragmentTvQuestionNumber;

    @BindView(R.id.offline_table_fragment_tv_choose_category)
    TextView offlineTableFragmentTvChooseCategory;
    @BindView(R.id.offline_table_fragment_tv_question)
    TextView offlineTableFragmentTvQuestion;
    @BindView(R.id.offline_table_fragment_btn_1)
    AppCompatButton offlineTableFragmentBtn1;
    @BindView(R.id.offline_table_fragment_btn_2)
    AppCompatButton offlineTableFragmentBtn2;
    @BindView(R.id.offline_table_fragment_btn_3)
    AppCompatButton offlineTableFragmentBtn3;
    @BindView(R.id.offline_table_fragment_btn_4)
    AppCompatButton offlineTableFragmentBtn4;
    @BindView(R.id.offline_table_fragment_cl_choose)
    ConstraintLayout offlineTableFragmentClChoose;

    @BindView(R.id.offline_table_fragment_tv_write_category)
    TextView offlineTableFragmentTvWriteCategory;
    @BindView(R.id.offline_table_fragment_tv_question_write)
    TextView offlineTableFragmentTvQuestionWrite;
    @BindView(R.id.offline_table_fragment_civ_cancel)
    ImageView offlineTableFragmentCivCancel;
    @BindView(R.id.offline_table_fragment_civ_check)
    ImageView offlineTableFragmentCivCheck;
    @BindView(R.id.offline_table_fragment_tv_write)
    TextView offlineTableFragmentTvWrite;
    @BindView(R.id.offline_table_fragment_rv_choose)
    RecyclerView offlineTableFragmentRvChoose;
    @BindView(R.id.offline_table_fragment_cl_write)
    ConstraintLayout offlineTableFragmentClWrite;
    private NavController navController;
    private String answer;
    private String lang = "ar";
    private TableWriteAdapter writeAdapter;
    private OfflineTableViewModel tableViewModel;
    private List<Question> questions = new ArrayList<>();
    private int questionNum = 0,  BET;
    private MediaPlayer correctMedia, wrongMedia, newQuestionMedia;
    private String category;
    private BroadcastReceiver mMessageReceiver;
    private int correct,resultId;
    private boolean sound;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public OfflineTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onBack() {

            new ExitDialog(this).show(getChildFragmentManager(), "exit");

    }
    @Override
    public void onStart() {
        super.onStart();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                if (intent.getStringExtra("type").equals("MyData")) {


                } else {

                    int coins = intent.getIntExtra("coins", 0);
                    int room = intent.getIntExtra("room", 1);
                    String image = intent.getStringExtra("image");
                    String name = intent.getStringExtra("name");
                    String category = intent.getStringExtra("category");
//
                    TableFragmentDirections.ActionTableFragmentToNotificationDialog action = TableFragmentDirections.actionTableFragmentToNotificationDialog();
                    action.setBet(coins);
                    action.setRoomId(room);
                    action.setImage(image);
                    action.setName(name);
                    action.setFlag(TableFragmentArgs.fromBundle(getArguments()).getFlag());
                    action.setLevel(TableFragmentArgs.fromBundle(getArguments()).getLevel());
                    action.setCategory(category);
                    action.setQsnLang(TableFragmentArgs.fromBundle(getArguments()).getQsnLang());
                    action.setSound(TableFragmentArgs.fromBundle(getArguments()).getSound());
                    action.setProfileId(TableFragmentArgs.fromBundle(getArguments()).getProfileId());
                    action.setMode("Accept");


                    try {
                        navController.navigate(action);
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("MyData"));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
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

        return inflater.inflate(R.layout.fragment_offline_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpActivity();
        AdView mAdView = view.findViewById(R.id.offline_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        resultId=new Random().nextInt();
        navController = Navigation.findNavController(view);
        BET = OfflineTableFragmentArgs.fromBundle(getArguments()).getQty();
        category = OfflineTableFragmentArgs.fromBundle(getArguments()).getCategory();
        lang = OfflineTableFragmentArgs.fromBundle(getArguments()).getLang();
        sound = OfflineTableFragmentArgs.fromBundle(getArguments()).getSound();


        tableViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(OfflineTableViewModel.class);
        if (sound) {
            correctMedia = MediaPlayer.create(getContext(), R.raw.correct);
            wrongMedia = MediaPlayer.create(getContext(), R.raw.wrong);


            newQuestionMedia = MediaPlayer.create(getContext(), R.raw.question);

        }

        compositeDisposable.add(tableViewModel.getQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Question>>() {
                    @Override
                    public void accept(List<Question> question) throws Exception {

                        Collections.shuffle(question);

                        for (int i = 0; i <BET ; i++) {

                            if(category.contains(question.get(i).getCategory())){
                                questions.add(question.get(i));
                            }
                        }

                        setData();
                    }
                }));

    }


    private void setData() {

        offlineTableFragmentTvQuestionNumber.setText("1 / "+questions.size());

        newQuestion(questionNum++);


    }

    private void newQuestion(int number) {


        if (number < questions.size()) {

            offlineTableFragmentTvQuestionNumber.setText(number + 1 + "\t /\t " + questions.size());

            answer = splitText(questions.get(number).getAnswer(), lang);
            setCategoryText(questions.get(number).getCategory());

            if (sound)
                newQuestionMedia.start();

            if (questions.get(number).getQuestionType() == 1) {

                offlineTableFragmentBtn1.setEnabled(true);
                offlineTableFragmentBtn2.setEnabled(true);
                offlineTableFragmentBtn3.setEnabled(true);
                offlineTableFragmentBtn4.setEnabled(true);

                offlineTableFragmentBtn1.setVisibility(View.VISIBLE);
               offlineTableFragmentBtn2.setVisibility(View.VISIBLE);
               offlineTableFragmentBtn3.setVisibility(View.VISIBLE);
               offlineTableFragmentBtn4.setVisibility(View.VISIBLE);


               offlineTableFragmentBtn1.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
               offlineTableFragmentBtn2.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
               offlineTableFragmentBtn3.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
               offlineTableFragmentBtn4.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));

               offlineTableFragmentClChoose.setVisibility(View.VISIBLE);
               offlineTableFragmentClWrite.setVisibility(View.GONE);

               offlineTableFragmentTvQuestion.setText(splitText(questions.get(number).getQuestion(), lang));
               offlineTableFragmentBtn1.setText(splitText(questions.get(number).getObtion1(), lang));
               offlineTableFragmentBtn2.setText(splitText(questions.get(number).getObtion2(), lang));
               offlineTableFragmentBtn3.setText(splitText(questions.get(number).getObtion3(), lang));
               offlineTableFragmentBtn4.setText(splitText(questions.get(number).getObtion4(), lang));


            } else if (questions.get(number).getQuestionType() == 2) {

                offlineTableFragmentCivCheck.setVisibility(View.VISIBLE);
                offlineTableFragmentCivCancel.setVisibility(View.VISIBLE);
                offlineTableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorBlack));


                offlineTableFragmentTvWrite.setText("");
                offlineTableFragmentClChoose.setVisibility(View.GONE);

                offlineTableFragmentClWrite.setVisibility(View.VISIBLE);
                offlineTableFragmentTvQuestionWrite.setText(splitText(questions.get(number).getQuestion(), lang));

                writeAdapter = new TableWriteAdapter(sortAnswer(answer, lang), this);

               offlineTableFragmentRvChoose.setLayoutManager(new GridLayoutManager(getContext(), 5));
               offlineTableFragmentRvChoose.setAdapter(writeAdapter);

//                setTimer(questions.get(number).getTime());
            }


        } else {

            resultNav();

        }

    }

    private void setCategoryText(String category) {
        switch (category) {
            case "1":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.islam));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.islam));
                break;
            case "2":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.geographic));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.geographic));
                break;
            case "3":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.historical));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.historical));
                break;
            case "4":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.puzzle));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.puzzle));
                break;
            case "5":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.science));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.science));
                break;
            case "6":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.sport));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.sport));
                break;
            case "7":
                offlineTableFragmentTvChooseCategory.setText(getString(R.string.technology));
                offlineTableFragmentTvWriteCategory.setText(getString(R.string.technology));
                break;
            case "8":
               offlineTableFragmentTvChooseCategory.setText(getString(R.string.math));
               offlineTableFragmentTvWriteCategory.setText(getString(R.string.math));
                break;
        }
    }


    private void resultNav() {

        // tableViewModel.setScore(p1Score, p2Score);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OfflineTableFragmentDirections.ActionOfflineTableFragmentToOfflineResultFragment action = OfflineTableFragmentDirections.actionOfflineTableFragmentToOfflineResultFragment();
                action.setHome(OfflineTableFragmentArgs.fromBundle(getArguments()).getHome());
                action.setResultId(resultId);
                action.setCorrect(correct);

                navController.navigate(action);


            }
        }, 2000);
    }


    private void selectedAnswer(TextView tableFragmentTvWrite) {

        offlineTableFragmentCivCheck.setVisibility(View.GONE);
        offlineTableFragmentCivCancel.setVisibility(View.GONE);

        int score = 0;

        if (tableFragmentTvWrite.getText().toString().equals(answer)) {
            tableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorGreen));
            correct++;
            if (TableFragmentArgs.fromBundle(getArguments()).getSound()) {
                correctMedia.start();

            }


        } else {
            tableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorRed));
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                wrongMedia.start();

        }

        if (questionNum <= questions.size())
            sendData(tableFragmentTvWrite.getText().toString(), score);

    }


    private void selectedAnswer(AppCompatButton tableFragmentBtn) {

        offlineTableFragmentBtn1.setEnabled(false);
        offlineTableFragmentBtn2.setEnabled(false);
        offlineTableFragmentBtn3.setEnabled(false);
        offlineTableFragmentBtn4.setEnabled(false);

        int score = 0;
        if (tableFragmentBtn.getText().toString().equals(answer)) {
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                correctMedia.start();
            correct++;

            tableFragmentBtn.setBackground(getResources().getDrawable(R.drawable.shape_correct_btn));


        } else {
            tableFragmentBtn.setBackground(getResources().getDrawable(R.drawable.shape_wrong_btn));
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                wrongMedia.start();

        }

        if (questionNum <= questions.size())
            sendData(tableFragmentBtn.getText().toString(), score);
    }

    private void sendData(String myAnswer, int score) {

        if (myAnswer.equals(""))
            myAnswer = " ";
        Answers answer = new Answers();

        answer.setAnswer(myAnswer);
        answer.setId(questionNum-1);
        answer.setResult(resultId);
        answer.setQuestion(questions.get(questionNum - 1).getQuestion());
        answer.setCorrect(questions.get(questionNum - 1).getAnswer());
        answer.setCategory(questions.get(questionNum - 1).getCategory());
        tableViewModel.setAnswer(answer);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                newQuestion(questionNum++);
            }
        },500);



    }


    private String sortAnswer(String text, String lang) {

        Random r = new Random();

        String answer = text;
        String ar = "ثقهضوجسشيتعقفرزظطكمش";
        String en = "EbrazxfgjlhtuiopmKLM";
        for (int i = 0; i < text.length(); i++) {
            if (lang.equals("ar")) {

                answer += ar.charAt(r.nextInt(en.length()));

            } else {

                answer += en.charAt(r.nextInt(en.length()));
            }

        }

        char[] words = answer.toCharArray();
        Arrays.sort(words);

        return String.valueOf(words);
    }


    private String splitText(String test, String lang) {

        String[] parts = test.split("[|]");
        String en = parts[0]; // 004
        String ar = parts[1];
        if (lang.equals("ar"))
            return ar.trim();
        else
            return en.trim();
    }


    @Override
    public void itemLetter(String title) {

       offlineTableFragmentTvWrite.append(title);
    }


    @Override
    public void getDataExitDialog() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navController.popBackStack();
            }
        }, 1000);

    }

    @OnClick({ R.id.offline_table_fragment_btn_1, R.id.offline_table_fragment_btn_2, R.id.offline_table_fragment_btn_3, R.id.offline_table_fragment_btn_4, R.id.offline_table_fragment_civ_cancel, R.id.offline_table_fragment_civ_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.offline_table_fragment_btn_1:
                  selectedAnswer(offlineTableFragmentBtn1);
                  break;
            case R.id.offline_table_fragment_btn_2:
                selectedAnswer(offlineTableFragmentBtn2);
                break;
            case R.id.offline_table_fragment_btn_3:
                selectedAnswer(offlineTableFragmentBtn3);
                break;
            case R.id.offline_table_fragment_btn_4:
                selectedAnswer(offlineTableFragmentBtn4);
                break;
            case R.id.offline_table_fragment_civ_cancel:
                writeAdapter.notifyDataSetChanged();

                offlineTableFragmentTvWrite.setText("");
                break;
            case R.id.offline_table_fragment_civ_check:
                selectedAnswer(offlineTableFragmentTvWrite);
                break;
        }
    }
}

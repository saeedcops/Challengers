package com.cops.challengers.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.adapters.TableWriteAdapter;
import com.cops.challengers.model.room.PlayersAnswer;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.model.room.Question;
import com.cops.challengers.model.room.Room;
import com.cops.challengers.view.dialogs.ExitDialog;
import com.cops.challengers.view.dialogs.InviteDialog;
import com.cops.challengers.view.dialogs.ProfileDialog;
import com.cops.challengers.view.dialogs.WinDialog;
import com.cops.challengers.viewModel.TableViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.cops.challengers.api.RetrofitClient.getWebSocket;
import static com.cops.challengers.utils.Util.format;

//import com.cops.challengers.model.Room;

/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends BaseFragment implements TableWriteAdapter.HomeAdapterClick, ExitDialog.OnExitChange, WinDialog.OnWinChange, InviteDialog.OnInviteClicked {


    @BindView(R.id.table_fragment_progressBar_receiver)
    ProgressBar tableFragmentProgressBarReceiver;
    @BindView(R.id.table_fragment_progressBar_sender)
    ProgressBar tableFragmentProgressBarSender;
    @BindView(R.id.table_fragment_image_sender)
    CircleImageView tableFragmentImageSender;
    @BindView(R.id.table_fragment_image_receiver)
    CircleImageView tableFragmentImageReceiver;
    @BindView(R.id.table_fragment_tv_sender_name)
    TextView tableFragmentTvSenderName;
    @BindView(R.id.table_fragment_tv_receiver_name)
    TextView tableFragmentTvReceiverName;
    @BindView(R.id.table_fragment_tv_result)
    TextView tableFragmentTvResult;
    @BindView(R.id.table_fragment_tv_question_number)
    TextView tableFragmentTvQuestionNumber;
    @BindView(R.id.table_fragment_tv_question)
    TextView tableFragmentTvQuestion;
    @BindView(R.id.table_fragment_btn_1)
    AppCompatButton tableFragmentBtn1;
    @BindView(R.id.table_fragment_btn_2)
    AppCompatButton tableFragmentBtn2;
    @BindView(R.id.table_fragment_btn_3)
    AppCompatButton tableFragmentBtn3;
    @BindView(R.id.table_fragment_btn_4)
    AppCompatButton tableFragmentBtn4;
    @BindView(R.id.table_fragment_cl_choose)
    ConstraintLayout tableFragmentClChoose;
    @BindView(R.id.table_fragment_tv_question_write)
    TextView tableFragmentTvQuestionWrite;
    @BindView(R.id.table_fragment_rv_choose)
    RecyclerView tableFragmentRvChoose;
    @BindView(R.id.table_fragment_cl_write)
    ConstraintLayout tableFragmentClWrite;
    @BindView(R.id.table_fragment_tv_write)
    TextView tableFragmentTvWrite;
    @BindView(R.id.table_fragment_civ_check)
    ImageView tableFragmentCivCheck;
    @BindView(R.id.table_fragment_card_write)
    CardView tableFragmentCardWrite;
    @BindView(R.id.table_fragment_sender_ccp)
    CountryCodePicker tableFragmentSenderCcp;
    @BindView(R.id.table_fragment_receiver_ccp)
    CountryCodePicker tableFragmentReceiverCcp;
    @BindView(R.id.table_fragment_civ_cancel)
    ImageView tableFragmentCivCancel;
    @BindView(R.id.table_fragment_tv_choose_category)
    TextView tableFragmentTvChooseCategory;
    @BindView(R.id.table_fragment_tv_write_category)
    TextView tableFragmentTvWriteCategory;
    @BindView(R.id.vs_fragment_tv_prize)
    TextView vsFragmentTvPrize;
    @BindView(R.id.vs_fragment_image_sender)
    CircleImageView vsFragmentImageSender;
    @BindView(R.id.vs_fragment_tv_score_sender)
    TextView vsFragmentTvScoreSender;
    @BindView(R.id.vs_fragment_sender_ccp)
    CountryCodePicker vsFragmentSenderCcp;
    @BindView(R.id.vs_fragment_tv_sender_name)
    TextView vsFragmentTvSenderName;
    @BindView(R.id.vs_fragment_image_receiver)
    CircleImageView vsFragmentImageReceiver;
    @BindView(R.id.vs_fragment_btn_invite)
    AppCompatButton vsFragmentBtnInvite;
    @BindView(R.id.vs_fragment_progress)
    LottieAnimationView vsFragmentProgress;
    @BindView(R.id.vs_fragment_tv_score_receiver)
    TextView vsFragmentTvScoreReceiver;
    @BindView(R.id.vs_fragment_receiver_ccp)
    CountryCodePicker vsFragmentReceiverCcp;
    @BindView(R.id.vs_fragment_tv_receiver_name)
    TextView vsFragmentTvReceiverName;
    @BindView(R.id.table_fragment_vs)
    ConstraintLayout tableFragmentVs;
    @BindView(R.id.table_fragment_table)
    ConstraintLayout tableFragmentTable;
    @BindView(R.id.table_fragment_iv_choose_trick)
    CircleImageView tableFragmentIvChooseTrick;
    @BindView(R.id.table_fragment_iv_write_trick)
    CircleImageView tableFragmentIvWriteTrick;
    @BindView(R.id.table_fragment_tv_choose_gem)
    TextView tableFragmentTvChooseGem;
    @BindView(R.id.table_fragment_tv_write_gem)
    TextView tableFragmentTvWriteGem;
    private NavController navController;
    private String answer;
    private String mode, lang = "ar";
    private TableWriteAdapter writeAdapter;
    private TableViewModel tableViewModel;
    private List<Question> questions = new ArrayList<>();
    private int questionNum = 0, p1Score = 0, p2Score = 0, playerNum, BET;
    private CountDownTimer countDownTimer;
    private Profile profile1 = new Profile();
    private Profile profile2 = new Profile();
    private CountDownTimer countDownTimerR;
    private boolean isAdded = false;
    private MediaPlayer correctMedia, wrongMedia, trickMedia, newQuestionMedia, leftMedia;
    private boolean left = false;
    private int userID;
    private int roomID;
    private String category;
    private SharedPreferences sharedPreferences;
    private String token;
    private WebSocket webSocket;
    private EchoWebSocketListener listener = new EchoWebSocketListener();
    private JSONObject jsonData = new JSONObject();
    private boolean player2Answered = false, player1Answered = false;
    private boolean isUpdated = false;
    private Integer profileId;
    private BroadcastReceiver mMessageReceiver;
    private int p1Gem, p2Gem;
    private boolean sound;
    private boolean isStarted = false;
    private boolean isRandom=false;


    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                if (intent.getStringExtra("type").equals("Challenge")) {

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
                    action.setUserId(userID);
                    action.setProfileId(TableFragmentArgs.fromBundle(getArguments()).getProfileId());
                    action.setMode("Accept");
//                    action.setToken(CategoryFragmentArgs.fromBundle(getArguments()).getT());


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
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onBack() {
        if (isStarted)
            new ExitDialog(this).show(getChildFragmentManager(), "exit");
        else
            super.onBack();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        // SETTING ADS
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpActivity();

        AdView mAdView = view.findViewById(R.id.table_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        navController = Navigation.findNavController(view);
        BET = TableFragmentArgs.fromBundle(getArguments()).getBet();
        category = TableFragmentArgs.fromBundle(getArguments()).getCategory();
        mode = TableFragmentArgs.fromBundle(getArguments()).getMode();
        userID = TableFragmentArgs.fromBundle(getArguments()).getUserID();
        profileId = TableFragmentArgs.fromBundle(getArguments()).getProfileId();
        lang = TableFragmentArgs.fromBundle(getArguments()).getQsnLang();
        roomID = TableFragmentArgs.fromBundle(getArguments()).getRoomId();
        sound = TableFragmentArgs.fromBundle(getArguments()).getSound();
        Log.i("RoomFrag",roomID+"");

        vsFragmentSenderCcp.setCountryForNameCode(TableFragmentArgs.fromBundle(getArguments()).getFlag());
        vsFragmentTvScoreSender.setText(TableFragmentArgs.fromBundle(getArguments()).getLevel() + "");
        vsFragmentTvSenderName.setText(TableFragmentArgs.fromBundle(getArguments()).getName());
        Glide.with(getContext()).load(TableFragmentArgs.fromBundle(getArguments()).getImage()).into(vsFragmentImageSender);


        tableViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(TableViewModel.class);
        if (sound) {
            correctMedia = MediaPlayer.create(getContext(), R.raw.correct);
            wrongMedia = MediaPlayer.create(getContext(), R.raw.wrong);
            trickMedia = MediaPlayer.create(getContext(), R.raw.trick);
            leftMedia = MediaPlayer.create(getContext(), R.raw.leave);
            newQuestionMedia = MediaPlayer.create(getContext(), R.raw.question);

        }
        if (mode.equals(getString(R.string.playfriend))) {
            isRandom=false;
            vsFragmentProgress.setVisibility(View.GONE);
            vsFragmentBtnInvite.setVisibility(View.VISIBLE);
            if (TableFragmentArgs.fromBundle(getArguments()).getFriendId() > 0) {
//                isStarted = true;

                tableViewModel.createRoom(token, category, BET, userID, TableFragmentArgs.fromBundle(getArguments()).getFriendId(), true);
            }

        } else if (mode.equals("Accept")) {

            isStarted = true;
            isRandom=false;
            tableViewModel.getRoom(token, roomID);

        } else {
            isStarted = true;
            tableViewModel.createRoom(token, category, BET, userID, 0, false);

        }
        tableViewModel.roomMutable.observe(getViewLifecycleOwner(), new Observer<Room>() {
            @Override
            public void onChanged(Room room) {

                if (room != null) {

                    roomID = room.getId();
                    Log.i("RoomFragIN",roomID+"");
                    webSocket = getWebSocket(token, listener, roomID);


                    try {
                        jsonData.put("player1score", -2);
                        jsonData.put("player2score", -2);
                        jsonData.put("question", questionNum);
                        jsonData.put("sender", userID);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!room.getAvaliable() && room.getPlayer2() != null
                            && mode.equals(getString(R.string.onetoone)) || mode.equals("Accept")) {

                        webSocket.send(jsonData.toString());

                        vsFragmentProgress.setVisibility(View.GONE);
                        vsFragmentTvScoreReceiver.setVisibility(View.VISIBLE);
                        isRandom=false;
                        isStarted=true;

                    } else {

                        webSocket.send(jsonData.toString());
                        if (!mode.equals(getString(R.string.playfriend)))
                            isRandom=true;
                        setNewRoom(room);


                    }
                }
            }
        });


    }

    private void joinToRoom(Room room) {


        isUpdated = true;
        vsFragmentBtnInvite.setVisibility(View.GONE);
        vsFragmentProgress.setVisibility(View.GONE);
        vsFragmentTvScoreReceiver.setVisibility(View.VISIBLE);

        Glide.with(getContext()).load(room.getPlayer1().getImage()).into(vsFragmentImageSender);
        vsFragmentSenderCcp.setCountryForNameCode(room.getPlayer1().getFlag());
        vsFragmentTvScoreSender.setText(String.valueOf(room.getPlayer1().getLevel()));
        vsFragmentTvSenderName.setText(room.getPlayer1().getName());

        vsFragmentReceiverCcp.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(room.getPlayer2().getImage()).into(vsFragmentImageReceiver);
        vsFragmentReceiverCcp.setCountryForNameCode(room.getPlayer2().getFlag());
        vsFragmentTvScoreReceiver.setText(String.valueOf(room.getPlayer2().getLevel()));
        vsFragmentTvReceiverName.setText(room.getPlayer2().getName());

        updateTable(room);
    }

    private void setNewRoom(Room room) {

        vsFragmentTvPrize.setText(format(BET * 2));

        Glide.with(getContext()).load(room.getPlayer1().getImage()).into(vsFragmentImageSender);
        vsFragmentSenderCcp.setCountryForNameCode(room.getPlayer1().getFlag());
        vsFragmentTvScoreSender.setText(String.valueOf(room.getPlayer1().getLevel()));
        vsFragmentTvSenderName.setText(room.getPlayer1().getName());

        if (isRandom) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRandom) {

                        tableViewModel.randomRoom(token, roomID);
                    }

                }
            }, 5000);
        }
    }

    private void updateTable(Room room) {


        if (room.getPlayer2() != null && questions.size() < 5) {
            tableFragmentVs.setVisibility(View.GONE);
            tableFragmentTable.setVisibility(View.VISIBLE);

            questions.addAll(room.getQuestions());
            profile1 = room.getPlayer1();
            profile2 = room.getPlayer2();
            p1Gem = profile1.getGem();
            p2Gem = profile2.getGem();
            if (profile1.getId() == profileId) {
                playerNum = 1;
                tableFragmentTvChooseGem.setText(p1Gem+"");
                tableFragmentTvWriteGem.setText(p1Gem+"");
            } else {
                playerNum = 2;
                tableFragmentTvChooseGem.setText(p2Gem+"");
                tableFragmentTvWriteGem.setText(p2Gem+"");
            }

            setData();

        }
    }


    private void setData() {

        tableFragmentTvQuestionNumber.setText("1 / 5");

        Glide.with(getContext()).load(profile1.getImage()).into(tableFragmentImageSender);
        tableFragmentSenderCcp.setCountryForNameCode(profile1.getFlag());
        tableFragmentTvSenderName.setText(profile1.getName());

        Glide.with(getContext()).load(profile2.getImage()).into(tableFragmentImageReceiver);
        tableFragmentReceiverCcp.setCountryForNameCode(profile2.getFlag());
        tableFragmentTvReceiverName.setText(profile2.getName());

        newQuestion(questionNum++);


    }

    private void newQuestion(int number) {


        if (number < questions.size()) {
            player2Answered = false;
            player1Answered = false;
            isAdded = false;
            setTimer(questions.get(number).getTime());

            tableFragmentTvResult.setText(p1Score + " \t - \t " + p2Score);
            tableFragmentTvQuestionNumber.setText(number + 1 + "\t /\t " + questions.size());

            answer = splitText(questions.get(number).getAnswer(), lang);
            setCategoryText(questions.get(number).getCategory());

            if (sound)
                newQuestionMedia.start();

            if (questions.get(number).getQuestionType() == 1) {

                tableFragmentBtn1.setEnabled(true);
                tableFragmentBtn2.setEnabled(true);
                tableFragmentBtn3.setEnabled(true);
                tableFragmentBtn4.setEnabled(true);

                tableFragmentBtn1.setVisibility(View.VISIBLE);
                tableFragmentBtn2.setVisibility(View.VISIBLE);
                tableFragmentBtn3.setVisibility(View.VISIBLE);
                tableFragmentBtn4.setVisibility(View.VISIBLE);
                tableFragmentIvChooseTrick.setVisibility(View.VISIBLE);

                tableFragmentBtn1.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
                tableFragmentBtn2.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
                tableFragmentBtn3.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));
                tableFragmentBtn4.setBackground(getResources().getDrawable(R.drawable.shape_answer_btn));

                tableFragmentClChoose.setVisibility(View.VISIBLE);
                tableFragmentClWrite.setVisibility(View.GONE);

                tableFragmentTvQuestion.setText(splitText(questions.get(number).getQuestion(), lang));
                tableFragmentBtn1.setText(splitText(questions.get(number).getObtion1(), lang));
                tableFragmentBtn2.setText(splitText(questions.get(number).getObtion2(), lang));
                tableFragmentBtn3.setText(splitText(questions.get(number).getObtion3(), lang));
                tableFragmentBtn4.setText(splitText(questions.get(number).getObtion4(), lang));


            } else if (questions.get(number).getQuestionType() == 2) {

                tableFragmentCivCheck.setVisibility(View.VISIBLE);
                tableFragmentCivCancel.setVisibility(View.VISIBLE);
                tableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorBlack));


                tableFragmentTvWrite.setText("");
                tableFragmentClChoose.setVisibility(View.GONE);
                tableFragmentIvWriteTrick.setVisibility(View.VISIBLE);
                tableFragmentClWrite.setVisibility(View.VISIBLE);
                tableFragmentTvQuestionWrite.setText(splitText(questions.get(number).getQuestion(), lang));

                writeAdapter = new TableWriteAdapter(sortAnswer(answer, lang), this);

                tableFragmentRvChoose.setLayoutManager(new GridLayoutManager(getContext(), 5));
                tableFragmentRvChoose.setAdapter(writeAdapter);

            }


        } else {

            resultNav();

        }

    }

    private void setCategoryText(String category) {
        switch (category) {
            case "1":
                tableFragmentTvChooseCategory.setText(getString(R.string.islam));
                tableFragmentTvWriteCategory.setText(getString(R.string.islam));
                break;
            case "2":
                tableFragmentTvChooseCategory.setText(getString(R.string.geographic));
                tableFragmentTvWriteCategory.setText(getString(R.string.geographic));
                break;
            case "3":
                tableFragmentTvChooseCategory.setText(getString(R.string.historical));
                tableFragmentTvWriteCategory.setText(getString(R.string.historical));
                break;
            case "4":
                tableFragmentTvChooseCategory.setText(getString(R.string.puzzle));
                tableFragmentTvWriteCategory.setText(getString(R.string.puzzle));
                break;
            case "5":
                tableFragmentTvChooseCategory.setText(getString(R.string.science));
                tableFragmentTvWriteCategory.setText(getString(R.string.science));
                break;
            case "6":
                tableFragmentTvChooseCategory.setText(getString(R.string.sport));
                tableFragmentTvWriteCategory.setText(getString(R.string.sport));
                break;
            case "7":
                tableFragmentTvChooseCategory.setText(getString(R.string.technology));
                tableFragmentTvWriteCategory.setText(getString(R.string.technology));
                break;
            case "8":
                tableFragmentTvChooseCategory.setText(getString(R.string.math));
                tableFragmentTvWriteCategory.setText(getString(R.string.math));
                break;
        }
    }


    private void resultNav() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webSocket.close(1000, "");
                TableFragmentDirections.ActionTableFragmentToResultFragment action = TableFragmentDirections.actionTableFragmentToResultFragment();
                action.setRoomID(roomID);
                action.setSound(TableFragmentArgs.fromBundle(getArguments()).getSound());
                action.setQsnLang(TableFragmentArgs.fromBundle(getArguments()).getQsnLang());
                action.setWin("OK");
                action.setUserId(userID);
                action.setProfileId(profileId);

                navController.navigate(action);


            }
        }, 2000);
    }

    private void setTimer(int time) {


        tableFragmentProgressBarReceiver.setProgress(time);
        tableFragmentProgressBarSender.setProgress(time);

        countDownTimer = new CountDownTimer(time * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

                Long percentage = millisUntilFinished / (time * 10);
                tableFragmentProgressBarSender.setProgress(percentage.intValue());
            }

            @Override
            public void onFinish() {

                if (playerNum == 1) {
                    try {
                        player1Answered = true;
                        jsonData.put("player1score", p1Score);
                        jsonData.put("player2score", p2Score);

                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                    sendData(getString(R.string.time_out), 0);
                }

            }
        };
        countDownTimer.start();


        countDownTimerR = new CountDownTimer(time * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

                Long percentage = millisUntilFinished / (time * 10);

                tableFragmentProgressBarReceiver.setProgress(percentage.intValue());

            }

            @Override
            public void onFinish() {

                if (playerNum == 2) {
                    try {
//                        jsonData.put("player1score",0);
                        player2Answered = true;
                        jsonData.put("player1score", p1Score);
                        jsonData.put("player2score", p2Score);

                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                    sendData(getString(R.string.time_out), 0);
                }

            }
        };
        countDownTimerR.start();

    }


    private void selectedAnswer(TextView tableFragmentTvWrite) {
        tableFragmentIvWriteTrick.setVisibility(View.INVISIBLE);
        tableFragmentCivCheck.setVisibility(View.GONE);
        tableFragmentCivCancel.setVisibility(View.GONE);

        int score = 0;

        if (tableFragmentTvWrite.getText().toString().equals(answer)) {
            tableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorGreen));
            if (TableFragmentArgs.fromBundle(getArguments()).getSound()) {
                correctMedia.start();
            }

            if (playerNum == 1) {
                countDownTimer.cancel();
                score = tableFragmentProgressBarSender.getProgress() / 2;
                player1Answered = true;
                p1Score += score;

            } else {

                countDownTimerR.cancel();
                score = tableFragmentProgressBarReceiver.getProgress() / 2;
                p2Score += score;
                player2Answered = true;
            }

            tableFragmentTvResult.setText(p1Score + " \t - \t " + p2Score);

        } else {
            tableFragmentTvWrite.setTextColor(getResources().getColor(R.color.colorRed));
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                wrongMedia.start();

            if (playerNum == 1) {
                countDownTimer.cancel();
                player1Answered = true;

            } else {
                player2Answered = true;
                countDownTimerR.cancel();

            }

        }
        try {

            jsonData.put("player1score", p1Score);
            jsonData.put("player2score", p2Score);


        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        if (questionNum <= questions.size())
            sendData(tableFragmentTvWrite.getText().toString(), score);

    }


    private void selectedAnswer(AppCompatButton tableFragmentBtn) {
        tableFragmentIvChooseTrick.setVisibility(View.INVISIBLE);
        tableFragmentBtn1.setEnabled(false);
        tableFragmentBtn2.setEnabled(false);
        tableFragmentBtn3.setEnabled(false);
        tableFragmentBtn4.setEnabled(false);

        int score = 0;
        if (tableFragmentBtn.getText().toString().equals(answer)) {
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                correctMedia.start();

            tableFragmentBtn.setBackground(getResources().getDrawable(R.drawable.shape_correct_btn));
            if (playerNum == 1) {

                countDownTimer.cancel();
                score = tableFragmentProgressBarSender.getProgress() / 2;
                p1Score += score;
                player1Answered = true;

            } else {

                countDownTimerR.cancel();
                score = tableFragmentProgressBarReceiver.getProgress() / 2;
                p2Score += score;
                player2Answered = true;

            }
            tableFragmentTvResult.setText(p1Score + " \t - \t " + p2Score);

        } else {
            tableFragmentBtn.setBackground(getResources().getDrawable(R.drawable.shape_wrong_btn));
            if (TableFragmentArgs.fromBundle(getArguments()).getSound())
                wrongMedia.start();

            if (playerNum == 1) {
                countDownTimer.cancel();
                player1Answered = true;

            } else {
                player2Answered = true;
                countDownTimerR.cancel();


            }

        }
        try {
            jsonData.put("player1score", p1Score);
            jsonData.put("player2score", p2Score);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        if (questionNum <= questions.size())
            sendData(tableFragmentBtn.getText().toString(), score);
    }

    private void sendData(String myAnswer, int score) {

        try {
            jsonData.put("sender", userID);
            jsonData.put("question", questionNum);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonData.toString());
        if (myAnswer.equals(""))
            myAnswer = " ";
        PlayersAnswer answer = new PlayersAnswer();
        if (playerNum == 1) {
            answer.setPlayer1Id(userID);
            answer.setPlayer2Id(0);
            answer.setPlayer1Score(score);
            answer.setPlayer1Answer(myAnswer);
        } else {
            answer.setPlayer1Id(0);
            answer.setPlayer2Id(userID);
            answer.setPlayer2Score(score);
            answer.setPlayer2Answer(myAnswer);
        }
        answer.setRoom(roomID);
        answer.setQuestion(questions.get(questionNum - 1).getQuestion());
        answer.setCorrect(questions.get(questionNum - 1).getAnswer());
        answer.setCategory(questions.get(questionNum - 1).getCategory());
        answer.setQuestionNum(questionNum);
        tableViewModel.setAnswer(answer);


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

    @OnClick({R.id.vs_fragment_btn_invite, R.id.table_fragment_iv_write_trick, R.id.table_fragment_iv_choose_trick, R.id.table_fragment_civ_cancel, R.id.table_fragment_civ_check, R.id.table_fragment_image_sender, R.id.table_fragment_image_receiver, R.id.table_fragment_btn_1, R.id.table_fragment_btn_2, R.id.table_fragment_btn_3, R.id.table_fragment_btn_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.table_fragment_image_sender:
                new ProfileDialog(profile1, true).show(getChildFragmentManager(), "profile");
                break;
            case R.id.table_fragment_image_receiver:
                new ProfileDialog(profile2, true).show(getChildFragmentManager(), "profile");
                break;
            case R.id.table_fragment_btn_1:
                selectedAnswer(tableFragmentBtn1);
                break;
            case R.id.table_fragment_btn_2:
                selectedAnswer(tableFragmentBtn2);
                break;
            case R.id.table_fragment_btn_3:
                selectedAnswer(tableFragmentBtn3);
                break;
            case R.id.table_fragment_btn_4:
                selectedAnswer(tableFragmentBtn4);

                break;
            case R.id.table_fragment_civ_cancel:

                writeAdapter.notifyDataSetChanged();
                tableFragmentTvWrite.setText("");

                break;
            case R.id.table_fragment_civ_check:

                selectedAnswer(tableFragmentTvWrite);

                break;

            case R.id.table_fragment_iv_choose_trick:

                List<AppCompatButton> btn = new ArrayList<>();
                btn.add(tableFragmentBtn1);
                btn.add(tableFragmentBtn2);
                btn.add(tableFragmentBtn3);
                btn.add(tableFragmentBtn4);
                Random rand = new Random();
                int n = rand.nextInt(4);
                tableFragmentIvChooseTrick.setVisibility(View.INVISIBLE);
                boolean iscOk = false;

                if (playerNum == 1 && p1Gem > 0) {
                    p1Gem -= 1;
                    iscOk = true;

                    tableFragmentTvChooseGem.setText(p1Gem+"");
                    tableFragmentTvWriteGem.setText(p1Gem+"");
                } else if (playerNum == 2 && p2Gem > 0) {
                    p2Gem -= 1;
                    iscOk = true;
                    tableFragmentTvChooseGem.setText(p2Gem+"");
                    tableFragmentTvWriteGem.setText(p2Gem+"");
                }

                if (iscOk) {
                    tableViewModel.takeGem(token);
                    if (sound)
                        trickMedia.start();

                    int count = 0;
                    while (count < 2) {
                        n = rand.nextInt(4);
                        if (!btn.get(n).getText().toString().equals(answer) && btn.get(n).getVisibility() == View.VISIBLE) {
                            count++;
                            btn.get(n).setVisibility(View.INVISIBLE);
                        }

                    }


                } else
                    Toast.makeText(baseActivity, getString(R.string.no_gems), Toast.LENGTH_SHORT).show();
                break;
            case R.id.table_fragment_iv_write_trick:
                tableFragmentIvWriteTrick.setVisibility(View.INVISIBLE);
                boolean isOk = false;

                if (playerNum == 1 && p1Gem > 0) {
                    p1Gem -= 1;
                    isOk = true;
                    tableFragmentTvChooseGem.setText(p1Gem+"");
                    tableFragmentTvWriteGem.setText(p1Gem+"");
                } else if (playerNum == 2 && p2Gem > 0) {
                    p2Gem -= 1;
                    isOk = true;
                    tableFragmentTvChooseGem.setText(p2Gem+"");
                    tableFragmentTvWriteGem.setText(p2Gem+"");
                }

                if (isOk) {
                    tableViewModel.takeGem(token);
                    if (sound)
                        trickMedia.start();

                    writeAdapter.notifyDataSetChanged();
                    tableFragmentTvWrite.setText("");

                    int size = answer.length() / 2;
                    for (int i = 0; i < size; i++) {
                        tableFragmentTvWrite.append(answer.charAt(i) + "");
                    }

                } else
                    Toast.makeText(baseActivity, getString(R.string.no_gems), Toast.LENGTH_SHORT).show();

                break;
            case R.id.vs_fragment_btn_invite:
                new InviteDialog(false, 500, token, this).show(getChildFragmentManager(), "invite");
                break;
        }
    }


    @Override
    public void itemLetter(String title) {

        tableFragmentTvWrite.append(title);
    }


    @Override
    public void getDataExitDialog() {


        try {
            if (playerNum == 1) {
                jsonData.put("player1score", -1);
                jsonData.put("player2score", p2Score);
                jsonData.put("question", questionNum);

            } else {
                jsonData.put("player1score", p1Score);
                jsonData.put("player2score", -1);
                jsonData.put("question", questionNum);
            }

            jsonData.put("sender", userID);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

//        if (isStarted) {
//            countDownTimer.cancel();
//            countDownTimerR.cancel();
//        }

        webSocket.send(jsonData.toString());
        webSocket.close(4001, "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navController.navigate(R.id.action_tableFragment_to_homeFragment);
            }
        }, 1000);

    }

    @Override
    public void sendToResult() {

        countDownTimer.cancel();
        countDownTimerR.cancel();
        webSocket.close(1000, "");
        TableFragmentDirections.ActionTableFragmentToResultFragment action = TableFragmentDirections.actionTableFragmentToResultFragment();
        action.setRoomID(roomID);
        action.setSound(TableFragmentArgs.fromBundle(getArguments()).getSound());
        action.setQsnLang(TableFragmentArgs.fromBundle(getArguments()).getQsnLang());
        action.setWin(getString(R.string.you_win));
        action.setUserId(userID);
        action.setProfileId(profileId);
        navController.navigate(action);

    }

    @Override
    public void getProfileId(int id, List<String> categories, int coins) {


        if (isStarted) {
            Log.i("RoomFragGetPro",roomID+"");
            tableViewModel.updateRoom(token, id, roomID);
        } else {

            isStarted = true;
            tableViewModel.createRoom(token, category, BET, userID, id, true);
        }
    }


    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.i("WSSocketO",response.message());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
//
            Log.i("WSSocketM",text);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(text);
                        int sender = (int) jsonObject.get("sender");
                        int p2s = (int) jsonObject.get("player2score");
                        int p1s = (int) jsonObject.get("player1score");
                        int qNum = (int) jsonObject.get("question");

                        if (playerNum == 1 && sender != userID) {


                            if (p2s == -1) {
                                if (sound) {
                                    leftMedia.start();
                                }

                                countDownTimer.cancel();
                                countDownTimerR.cancel();
                                WinDialog winDialog = new WinDialog(profile2.getName(), TableFragment.this);
                                winDialog.setCancelable(false);
                                winDialog.show(getChildFragmentManager(), "win");


                                left = true;

                            } else if (p2s >= 0) {
                                player2Answered = true;

                                p2Score = p2s;
                                countDownTimerR.cancel();
                            }

                        } else if (playerNum == 2 && sender != userID) {


                            if (p1s == -1) {
                                if (sound) {
                                    leftMedia.start();
                                }
//                                client.dispatcher().cancelAll();
                                countDownTimer.cancel();
                                countDownTimerR.cancel();
                                WinDialog winDialog = new WinDialog(profile1.getName(), TableFragment.this);
                                winDialog.setCancelable(false);
                                winDialog.show(getChildFragmentManager(), "win");


                                left = true;
                            } else if (p1s >= 0) {
                                player1Answered = true;
                                p1Score = p1s;
                                countDownTimer.cancel();

                            }
                        } else if (p1s == -2 && p2s == -2 && sender != userID && !isUpdated) {

                            isRandom=false;
                            getUpdatedData();
                        }

                        tableFragmentTvResult.setText(p1Score + " \t - \t " + p2Score);
                        if (player1Answered && player2Answered && !isAdded && qNum == questionNum && !left) {
                            isAdded = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newQuestion(questionNum++);
                                }
                            }, 1000);

                        }
                        if (p2s == -3)
                            Toast.makeText(getContext(), getString(R.string.reject), Toast.LENGTH_LONG).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {

            webSocket.close(NORMAL_CLOSURE_STATUS, null);


        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {


        }
    }

    private void getUpdatedData() {
        tableViewModel.getRoom(token, roomID);
        tableViewModel.roomMutable.observe(getViewLifecycleOwner(), new Observer<Room>() {
            @Override
            public void onChanged(Room room) {

                if (room.getPlayer2() != null) {

                    joinToRoom(room);
                }
            }
        });
    }

}

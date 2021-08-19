package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;

import org.json.JSONException;
import org.json.JSONObject;
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

public class NotificationDialog extends DialogFragment {


    @BindView(R.id.notification_dialog_civ_image)
    CircleImageView notificationDialogCivImage;
    @BindView(R.id.notification_dialog_tv_name)
    TextView notificationDialogTvName;
    @BindView(R.id.notification_dialog_tv_coins)
    TextView notificationDialogTvCoins;
    @BindView(R.id.notification_dialog_tv_category)
    TextView notificationDialogTvCategory;

    private int coins,roomId,userId,profileId,level;
    private String category,name,image,token,lang,flag;
    private boolean sound;
    private WebSocket webSocket;
    private EchoWebSocketListener listener = new EchoWebSocketListener();
    private SharedPreferences sharedPreferences = null;
    private NavController navController;
    private MediaPlayer challengeMedia= new MediaPlayer();

    public NotificationDialog() {
    }


    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        dialog.getWindow().setGravity(Gravity.TOP);

        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        token= sharedPreferences.getString("token", null);
        challengeMedia.reset();
        challengeMedia = MediaPlayer.create(getParentFragment().getContext(), R.raw.chalenge);
        getDialog().setCancelable(false);
        return inflater.inflate(R.layout.dialog_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        challengeMedia.start();
        navController = Navigation.findNavController(getParentFragment().getView());

        coins = NotificationDialogArgs.fromBundle(getArguments()).getBet();
        category = NotificationDialogArgs.fromBundle(getArguments()).getCategory();
        flag = NotificationDialogArgs.fromBundle(getArguments()).getFlag();
        level = NotificationDialogArgs.fromBundle(getArguments()).getLevel();
        name = NotificationDialogArgs.fromBundle(getArguments()).getName();
        image = NotificationDialogArgs.fromBundle(getArguments()).getImage();
        roomId = NotificationDialogArgs.fromBundle(getArguments()).getRoomId();
        lang = NotificationDialogArgs.fromBundle(getArguments()).getQsnLang();
        sound = NotificationDialogArgs.fromBundle(getArguments()).getSound();
        userId = NotificationDialogArgs.fromBundle(getArguments()).getUserId();
        profileId = NotificationDialogArgs.fromBundle(getArguments()).getProfileId();

        notificationDialogTvCoins.setText(getString(R.string.challenged_for)+" "+format(coins));
        notificationDialogTvCategory.setText(getString(R.string.category)+" : "+category);
        notificationDialogTvName.setText(name);
        Glide.with(getActivity()).load(image).into(notificationDialogCivImage);



    }

    @OnClick({R.id.notification_dialog_btn_ok, R.id.notification_dialog_btn_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.notification_dialog_btn_ok:
                NotificationDialogDirections.ActionNotificationDialogToTableFragment action = NotificationDialogDirections.actionNotificationDialogToTableFragment();
                action.setBet(coins);
                action.setFlag(flag);
                action.setLevel(level);
                action.setCategory(category);
                action.setUserID(userId);
                action.setProfileId(profileId);
                action.setImage(image);
                action.setName(name);
                action.setQsnLang(lang);
                action.setSound(sound);
                action.setRoomId(roomId);
                action.setMode("Accept");

                navController.navigate(action);

                getDialog().dismiss();
                break;
            case R.id.notification_dialog_btn_no:
                webSocket = getWebSocket(token, listener, roomId);
                JSONObject json = new JSONObject();
                try {
                    json.put("player1score",-2);
                    json.put("player2score",-3);
                    json.put("question", 0);
                    json.put("sender",roomId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                webSocket.send(json.toString());
                getDialog().dismiss();
                break;
        }
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
//


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
//            Log.i("onFailure",t.getMessage());

        }
    }
}

package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.adapters.ImageAdapter;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.viewModel.ProfileViewModel;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cops.challengers.utils.Util.format;

public class ProfileDialog extends DialogFragment implements ImageAdapter.ImageAdapterClick {


    @BindView(R.id.profile_dialog_civ_user)
    CircleImageView profileDialogCivUser;
    @BindView(R.id.profile_dialog_tv_level)
    TextView profileDialogTvLevel;
    @BindView(R.id.profile_dialog_ccp)
    CountryCodePicker profileDialogCcp;
    @BindView(R.id.profile_dialog_tv_name)
    EditText profileDialogTvName;
    @BindView(R.id.profile_dialog_tv_gold)
    TextView profileDialogTvGold;
    @BindView(R.id.profile_dialog_tv_score)
    TextView profileDialogTvScore;
    @BindView(R.id.profile_dialog_tv_match)
    TextView profileDialogTvMatch;
    @BindView(R.id.profile_dialog_tv_win)
    TextView profileDialogTvWin;
    @BindView(R.id.profile_dialog_tv_loss)
    TextView profileDialogTvLoss;
    @BindView(R.id.profile_dialog_tv_league)
    TextView profileDialogTvLeague;
    @BindView(R.id.profile_dialog_cl_status)
    ConstraintLayout profileDialogClStatus;
    @BindView(R.id.profile_dialog_rv)
    RecyclerView profileDialogRv;
    @BindView(R.id.profile_dialog_cl_image)
    ConstraintLayout profileDialogClImage;
    @BindView(R.id.profile_dialog_btn_save)
    AppCompatButton profileDialogBtnSave;
    @BindView(R.id.profile_dialog_civ_edit)
    CircleImageView profileDialogCivEdit;
    private ProfileViewModel profileViewModel;
    private ImageAdapter imageAdapter;
    private String image;
    private Profile user;
    private OnProfileChange onProfileChange;
    private boolean isShow = false;
    private String token;

    public ProfileDialog() {
    }


    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();

    }

    public ProfileDialog(Profile user, boolean isShow) {
        this.user = user;
        this.isShow = isShow;

    }

    public ProfileDialog(String token,Profile user, OnProfileChange onProfileChange) {
        this.onProfileChange = onProfileChange;
        this.user = user;
    }

    public interface OnProfileChange {
        void getProfileData(String image, String flag, String name);
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setView();

        profileDialogTvName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                profileDialogBtnSave.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        profileViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ProfileViewModel.class);

        List<String> images=new ArrayList<>();
        for (int i = 1; i < 10 ; i++) {

            images.add("http://192.168.10.143:8080/static/media/"+i+".png");
        }

        imageAdapter = new ImageAdapter(user.getImage(), images, ProfileDialog.this);
        profileDialogRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        profileDialogRv.setAdapter(imageAdapter);


        profileDialogCcp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                profileDialogBtnSave.setVisibility(View.VISIBLE);
            }
        });


    }

    private void setView() {
        if (isShow) {
            profileDialogCcp.setCcpClickable(false);
            profileDialogTvName.setEnabled(false);
            profileDialogCivEdit.setVisibility(View.GONE);

        }

        image = user.getImage();
        Glide.with(getContext()).load(image).into(profileDialogCivUser);
        profileDialogTvLevel.setText(String.valueOf(user.getLevel()));
        profileDialogCcp.setCountryForNameCode(user.getFlag());
        profileDialogTvName.setText(user.getName());
        profileDialogTvGold.setText(format(user.getCoins()));
        profileDialogTvScore.setText(String.valueOf(user.getScore()));

        profileDialogTvMatch.setText(String.valueOf(user.getLoss() + user.getWin()));
        profileDialogTvWin.setText(String.valueOf(user.getWin()));
        profileDialogTvLoss.setText(String.valueOf(user.getLoss()));

        switch (user.getMyLeague()){

            case 1:
                profileDialogTvLeague.setText("Bronze");
                break;
            case 2:
                profileDialogTvLeague.setText("Silver");
                break;
            case 3:
                profileDialogTvLeague.setText("Gold");
                break;
            case 4:
                profileDialogTvLeague.setText("Diamond");
                break;
            case 5:
                profileDialogTvLeague.setText("Legendary");
                break;
        }

    }

    @OnClick({R.id.profile_dialog_btn_exit, R.id.profile_dialog_civ_edit, R.id.profile_dialog_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_dialog_btn_exit:

                getDialog().dismiss();
                break;

            case R.id.profile_dialog_civ_edit:
                profileDialogClImage.setVisibility(View.VISIBLE);
                profileDialogBtnSave.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_dialog_btn_save:

                Glide.with(getContext()).load(image).into(profileDialogCivUser);
              profileViewModel.saveData(token,image, profileDialogCcp.getSelectedCountryNameCode(), profileDialogTvName.getText().toString());
                profileDialogClImage.setVisibility(View.GONE);
                profileDialogBtnSave.setVisibility(View.GONE);
                onProfileChange.getProfileData(image, profileDialogCcp.getSelectedCountryNameCode(), profileDialogTvName.getText().toString());
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        profileDialogBtnSave.setVisibility(View.GONE);
    }

    @Override
    public void itemLetter(String title) {

        image = title;
        imageAdapter.setSelectedImage(title);
        profileDialogRv.setAdapter(imageAdapter);

    }
}

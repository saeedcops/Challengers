package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cops.challengers.R;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.view.MainActivity;
import com.cops.challengers.viewModel.DeleteViewModel;
import com.facebook.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExitDialog extends DialogFragment {


    @BindView(R.id.tv_exit)
    TextView tvExit;
    private String text = "";

    private OnExitChange onExitChange;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor;
    private DeleteViewModel deleteViewModel;

    public interface OnExitChange {
        void getDataExitDialog();
    }

    public ExitDialog(String text) {
        this.text = text;
    }

    public ExitDialog(OnExitChange onExitChange) {
        this.onExitChange = onExitChange;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_exit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (!text.equals("")) {
            tvExit.setText(text);
            deleteViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DeleteViewModel.class);
            sharedPreferences = getActivity().getSharedPreferences(
                    "challengers", getActivity().MODE_PRIVATE);
            editor = sharedPreferences.edit();

        }
    }

    @OnClick({R.id.exit_dialog_btn_exit, R.id.exit_dialog_btn_no, R.id.exit_dialog_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.exit_dialog_btn_no:
                getDialog().dismiss();
                break;
            case R.id.exit_dialog_btn_ok:
                if (text.equals("")) {
                    onExitChange.getDataExitDialog();
                    getDialog().dismiss();

                } else {

                    String token = sharedPreferences.getString("token", "");
                    int profileId = sharedPreferences.getInt("profile_id", 0);
                    deleteViewModel.deleteUser(token, profileId);

                    deleteViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
                        @Override
                        public void onChanged(Profile profile) {
                            if (profile != null) {

                                LoginManager.getInstance().logOut();
                                editor.clear();
                                editor.apply();

                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            } else {
                                Toast.makeText(getContext(), "noData", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                break;
        }
    }
}

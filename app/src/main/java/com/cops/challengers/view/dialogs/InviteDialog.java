package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;
import com.cops.challengers.adapters.InviteAdapter;
import com.cops.challengers.adapters.InviteCategoryAdapter;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.viewModel.InviteViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cops.challengers.utils.Util.format;


public class InviteDialog extends DialogFragment implements InviteAdapter.OnInviteClicked, InviteCategoryAdapter.OnInviteClicked {

    @BindView(R.id.invite_dialog_rv)
    RecyclerView inviteDialogRv;
    @BindView(R.id.invite_dialog_rv_category)
    RecyclerView inviteDialogRvCategory;
    @BindView(R.id.invite_fragment_tv_cost)
    TextView inviteFragmentTvCost;
    @BindView(R.id.invite_dialog_rl)
    RelativeLayout inviteDialogRl;
    @BindView(R.id.invite_dialog_iv_no_friend)
    ImageView inviteDialogIvNoFriend;
    @BindView(R.id.invite_dialog_tv_no_friend)
    TextView inviteDialogTvNoFriend;
    private String token;
    private InviteViewModel inviteViewModel;
    private OnInviteClicked onInviteClicked;
    private boolean show;
    private List<String> categories = new ArrayList<>();
    private int coins,current=500;
    private int position = 0;
    private final int BET[] = new int[]{500, 1000, 10000, 50000, 100000, 1000000,2000000,5000000};


    public interface OnInviteClicked {

        void getProfileId(int id, List<String> categories, int coins);
    }

    public InviteDialog(boolean show, int coins, String token, OnInviteClicked onInviteClicked) {
        this.token = token;
        this.onInviteClicked = onInviteClicked;
        this.show = show;
        this.coins = coins;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        inviteViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(InviteViewModel.class);
        inviteViewModel.getUserFriends(token);
        return inflater.inflate(R.layout.dialog_invite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (show) {


            inviteDialogRvCategory.setVisibility(View.VISIBLE);
            inviteDialogRl.setVisibility(View.VISIBLE);
            inviteDialogRvCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));
            inviteDialogRvCategory.setAdapter(new InviteCategoryAdapter(this));
        }

        inviteViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                if (profiles != null) {

                    if (profiles.size()<1) {
                        inviteDialogIvNoFriend.setVisibility(View.VISIBLE);
                        inviteDialogTvNoFriend.setVisibility(View.VISIBLE);
                    }

                    inviteDialogRv.setLayoutManager(new LinearLayoutManager(getContext()));
                    inviteDialogRv.setAdapter(new InviteAdapter(profiles, InviteDialog.this));
                }

            }
        });

    }

    @OnClick({R.id.invite_dialog_btn_exit, R.id.invite_fragment_civ_remove, R.id.invite_fragment_civ_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.invite_fragment_civ_remove:
                remove();
                break;
            case R.id.invite_fragment_civ_add:
                add();
                break;

            case R.id.invite_dialog_btn_exit:
                getDialog().dismiss();
                break;

        }
    }

    private void remove() {

        if (current == BET[0])
            return;

        current=BET[--position];
        inviteFragmentTvCost.setText(format(current));

        if (current <= coins) {

            inviteFragmentTvCost.setAlpha(1f);
            inviteFragmentTvCost.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    private void add() {


        if (current == BET[7])
            return;
        current=BET[++position];
        inviteFragmentTvCost.setText(format(current));

        if (current > coins) {
            inviteFragmentTvCost.setAlpha(0.5f);
            inviteFragmentTvCost.setTextColor(getResources().getColor(R.color.colorRed));
        }

    }

    @Override
    public void clicked(int id) {

        if (show) {
            if (categories.size() > 0) {
                if (inviteFragmentTvCost.getTextColors().getDefaultColor() == getResources().getColor(R.color.colorRed)) {

                    Toast.makeText(getContext(), getString(R.string.no_enough_coins), Toast.LENGTH_SHORT).show();

                } else {

                    onInviteClicked.getProfileId(id, categories, current);
                    getDialog().dismiss();

                }

            } else

                Toast.makeText(getContext(), getString(R.string.at_least_select_category), Toast.LENGTH_LONG).show();
        } else {

            onInviteClicked.getProfileId(id, categories, coins);
            getDialog().dismiss();
        }
    }

    @Override
    public void setCategories(List<String> categories) {

        this.categories = categories;
    }

    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();

    }
}

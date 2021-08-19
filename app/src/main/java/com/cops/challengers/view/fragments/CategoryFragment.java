package com.cops.challengers.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cops.challengers.R;
import com.cops.challengers.adapters.CategoryAdapter;
import com.cops.challengers.viewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.cops.challengers.utils.Util.format;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends BaseFragment implements CategoryAdapter.CategoryViewEdit {


    @BindView(R.id.category_fragment_tv_title)
    TextView categoryFragmentTvTitle;
    @BindView(R.id.category_fragment_rv)
    RecyclerView categoryFragmentRv;
    @BindView(R.id.category_fragment_tv_cost)
    TextView categoryFragmentTvCost;
    @BindView(R.id.category_fragment_rv_online)
    RelativeLayout categoryFragmentRvOnline;
    @BindView(R.id.category_fragment_ci_download)
    CircleImageView categoryFragmentCiDownload;
    @BindView(R.id.category_fragment_tv_download)
    TextView categoryFragmentTvDownload;
    @BindView(R.id.category_fragment_progress_download)
    ProgressBar categoryFragmentProgressDownload;
    @BindView(R.id.category_fragment_tv_progress)
    TextView categoryFragmentTvProgress;
    @BindView(R.id.category_fragment_rv_offline)
    RelativeLayout categoryFragmentRvOffline;
    @BindView(R.id.category_fragment_tv_count)
    TextView categoryFragmentTvCount;
    private CategoryAdapter adapter;
    private NavController navController;
    private String mode = "";
    private  int BET[] = new int[]{500, 1000, 10000, 50000, 100000, 1000000,2000000,5000000};
    private int position = 0;
    private List<String> categories = new ArrayList<>();
    private int coins;
    private int userID;
    private CategoryViewModel categoryViewModel;
    private BroadcastReceiver mMessageReceiver;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String token;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int addCurrent=500;


    public CategoryFragment() {
        // Required empty public constructor

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

                    try {
                        CategoryFragmentDirections.ActionCategoryFragmentToNotificationDialog action = CategoryFragmentDirections.actionCategoryFragmentToNotificationDialog();
                        action.setBet(coins);
                        action.setRoomId(room);
                        action.setImage(image);
                        action.setName(name);
                        action.setFlag(CategoryFragmentArgs.fromBundle(getArguments()).getFlag());
                        action.setLevel(CategoryFragmentArgs.fromBundle(getArguments()).getLevel());
                        action.setCategory(category);
                        action.setQsnLang(CategoryFragmentArgs.fromBundle(getArguments()).getQsnLang());
                        action.setSound(CategoryFragmentArgs.fromBundle(getArguments()).getSound());
                        action.setUserId(userID);
                        action.setProfileId(CategoryFragmentArgs.fromBundle(getArguments()).getProfileId());
                        action.setMode("Accept");
//                    action.setToken(CategoryFragmentArgs.fromBundle(getArguments()).getT());


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
    public void onDestroy() {
        getViewModelStore().clear();
        compositeDisposable.dispose();
        super.onDestroy();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpActivity();
        token = sharedPreferences.getString("token", null);
        mode = CategoryFragmentArgs.fromBundle(getArguments()).getMode();
        userID = CategoryFragmentArgs.fromBundle(getArguments()).getUserID();
        coins = CategoryFragmentArgs.fromBundle(getArguments()).getCoins();
        navController = Navigation.findNavController(view);
        categoryViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(CategoryViewModel.class);

        categoryFragmentTvTitle.setText(mode);


        setRV();

        if (mode.equals(getString(R.string.offline))) {
            BET = new int[]{5, 10, 15, 20};
            addCurrent=5;
            categoryFragmentTvCost.setText(BET[0]+"");
            categoryFragmentTvCount.setText(getString(R.string.number_of_questions));
            if (sharedPreferences.getString("questions", null) == null) {

                categoryFragmentRvOffline.setVisibility(View.VISIBLE);
                categoryFragmentRvOnline.setVisibility(View.GONE);

            }

            categoryViewModel.questionsMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
//                    categoryFragmentProgressDownload.setProgress(integer);
//                    categoryFragmentTvProgress.setText(integer + "%");

                    if (integer == 100) {
                        editor.putString("questions", "ok");
                        editor.apply();
                        categoryFragmentRvOffline.setVisibility(View.GONE);
                        categoryFragmentRvOnline.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }


    private void setRV() {

        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");


        adapter = new CategoryAdapter(this, categories);

        categoryFragmentRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryFragmentRv.setAdapter(adapter);
    }


    @OnClick({R.id.category_fragment_ci_download, R.id.category_fragment_civ_exit, R.id.category_fragment_civ_remove, R.id.category_fragment_civ_add, R.id.category_fragment_btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.category_fragment_ci_download:

                categoryFragmentProgressDownload.setVisibility(View.VISIBLE);
                categoryFragmentTvProgress.setVisibility(View.VISIBLE);

                categoryFragmentTvDownload.setVisibility(View.GONE);
                categoryFragmentCiDownload.setVisibility(View.GONE);
                categoryViewModel.getQuestions(token);
                break;
            case R.id.category_fragment_civ_exit:
                navController.popBackStack();
                break;
            case R.id.category_fragment_civ_remove:

                remove();

                break;
            case R.id.category_fragment_civ_add:

                add();

                break;
            case R.id.category_fragment_btn_start:

                if (categories.size() > 0) {
                    if (categoryFragmentTvCost.getTextColors().getDefaultColor() == getResources().getColor(R.color.colorRed)) {

                        Toast.makeText(getContext(), getString(R.string.no_enough_coins), Toast.LENGTH_SHORT).show();

                    } else {

                        if (!mode.equals(getString(R.string.offline))) {


                            CategoryFragmentDirections.ActionCategoryFragmentToTableFragment action = CategoryFragmentDirections.actionCategoryFragmentToTableFragment();
                            action.setBet(addCurrent);
                            action.setCategory(categories.toString());
                            action.setMode(mode);
                            action.setUserID(userID);
                            action.setProfileId(CategoryFragmentArgs.fromBundle(getArguments()).getProfileId());
                            action.setImage(CategoryFragmentArgs.fromBundle(getArguments()).getImage());
                            action.setFlag(CategoryFragmentArgs.fromBundle(getArguments()).getFlag());
                            action.setName(CategoryFragmentArgs.fromBundle(getArguments()).getName());
                            action.setLevel(CategoryFragmentArgs.fromBundle(getArguments()).getLevel());
                            action.setQsnLang(CategoryFragmentArgs.fromBundle(getArguments()).getQsnLang());
                            action.setSound(CategoryFragmentArgs.fromBundle(getArguments()).getSound());

                            navController.navigate(action);
                        }else{

                            CategoryFragmentDirections.ActionCategoryFragmentToOfflineTableFragment action = CategoryFragmentDirections.actionCategoryFragmentToOfflineTableFragment();
                            action.setQty(addCurrent);
                            action.setCategory(categories.toString());
                            action.setHome(CategoryFragmentArgs.fromBundle(getArguments()).getHome());
                            action.setLang(CategoryFragmentArgs.fromBundle(getArguments()).getQsnLang());
                            action.setSound(CategoryFragmentArgs.fromBundle(getArguments()).getSound());
                            navController.navigate(action);
                        }
                    }

                } else

                    Toast.makeText(getContext(), getString(R.string.at_least_select_category), Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void remove() {

        if (addCurrent == BET[0])
            return;

        addCurrent=BET[--position];
        categoryFragmentTvCost.setText(format(addCurrent));

        if (addCurrent <= coins) {

            categoryFragmentTvCost.setAlpha(1f);
            categoryFragmentTvCost.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    private void add() {

        if(mode.equals(getString(R.string.offline))) {
            if (addCurrent == BET[3])
                return;
        } else {
            if (addCurrent == BET[7])
                return;
        }
        addCurrent=BET[++position];
        categoryFragmentTvCost.setText(format(addCurrent));

        if (addCurrent > coins) {
            categoryFragmentTvCost.setAlpha(0.5f);
            categoryFragmentTvCost.setTextColor(getResources().getColor(R.color.colorRed));
        }

    }

    @Override
    public void onSelectView(List<String> categories) {
        this.categories = categories;

    }

}

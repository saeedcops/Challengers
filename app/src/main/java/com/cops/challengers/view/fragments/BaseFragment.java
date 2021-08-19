package com.cops.challengers.view.fragments;



import androidx.fragment.app.Fragment;

import com.cops.challengers.view.MainActivity;

public class BaseFragment extends Fragment {

    public MainActivity baseActivity;

    public void setUpActivity() {
        baseActivity = (MainActivity) getActivity();

        baseActivity.baseFragment = this;
    }

    public void onBack() {
        baseActivity.superBackPressed();
    }

}

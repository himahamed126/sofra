package com.example.sofra.ui;

import androidx.fragment.app.Fragment;

import com.example.sofra.ui.activity.AuthActivity;
import com.example.sofra.ui.activity.BaseActivity;
import com.example.sofra.ui.activity.HomeActivity;

public class BaseFragment extends Fragment {
    protected BaseActivity baseActivity;
    protected AuthActivity authActivity;
    protected HomeActivity homeActivity;

    protected void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    protected void setupHomeActivity() {
        homeActivity = (HomeActivity) getActivity();
    }

    public void onBack() {
        baseActivity.superOnBackPressed();
    }
}

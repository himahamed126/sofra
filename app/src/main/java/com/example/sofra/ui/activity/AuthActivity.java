package com.example.sofra.ui.activity;

import android.os.Bundle;

import com.example.sofra.R;
import com.example.sofra.ui.fragment.authCycle.LoginFragment;

import static com.example.sofra.helper.HelperMethod.replace;

public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        LoginFragment loginFragment = new LoginFragment();

        replace(loginFragment, getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}

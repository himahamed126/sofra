package com.example.sofra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sofra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.data.local.SharedPreferencesManger.*;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.fragment_splash_btn_request_food)
    Button fragmentSplashBtnRequestFood;
    @BindView(R.id.fragment_splash_btn_sell_food)
    Button fragmentSplashBtnSellFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setSharedPreferences(this);
    }

    @OnClick({R.id.fragment_splash_btn_request_food, R.id.fragment_splash_btn_sell_food})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_splash_btn_request_food:
                Intent intent = new Intent(this, HomeActivity.class);
                SaveData(this, USER_TYPE, "client");
                startActivity(intent);
                break;
            case R.id.fragment_splash_btn_sell_food:
                intent = new Intent(this, AuthActivity.class);
                SaveData(this, USER_TYPE, "restaurant");
                startActivity(intent);
                break;
        }
    }
}

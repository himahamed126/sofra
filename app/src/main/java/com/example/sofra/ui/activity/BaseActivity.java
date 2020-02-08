package com.example.sofra.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sofra.ui.BaseFragment;

public class BaseActivity extends AppCompatActivity {
    public BaseFragment baseFragment;


    public void superOnBackPressed() {
        super.onBackPressed();
    }
}

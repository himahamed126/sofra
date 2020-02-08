package com.example.sofra.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;

import butterknife.ButterKnife;

public class EmptyFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

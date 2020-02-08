package com.example.sofra.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.sofra.R;
import com.example.sofra.ui.activity.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.clean;

public class LogOutDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_log_out, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @OnClick({R.id.dialog_sign_up_btn_yes, R.id.dialog_sign_up_btn_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_sign_up_btn_yes:
                clean(getActivity());
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
                break;
            case R.id.dialog_sign_up_btn_no:
                getDialog().cancel();
                break;
        }
    }
}

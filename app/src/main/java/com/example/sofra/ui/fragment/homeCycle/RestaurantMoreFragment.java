package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.sofra.R;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.dialog.LogOutDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.helper.HelperMethod.replace;

public class RestaurantMoreFragment extends BaseFragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_more, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick({R.id.fragment_restaurant_more_btn_offer, R.id.fragment_restaurant_more_btn_connect_us, R.id.fragment_restaurant_more_btn_about_app, R.id.fragment_restaurant_more_btn_comment_rate, R.id.fragment_restaurant_more_btn_change_pass, R.id.fragment_restaurant_more_btn_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_restaurant_more_btn_offer:
                RestaurantOfferListFragment restaurantOfferListFragment = new RestaurantOfferListFragment();
                replace(restaurantOfferListFragment, getFragmentManager(), R.id.activity_home_fl_frame, null, null);
                break;
            case R.id.fragment_restaurant_more_btn_connect_us:
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                replace(contactUsFragment, getActivity().getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                break;
            case R.id.fragment_restaurant_more_btn_about_app:
                AboutAppFragment aboutAppFragment = new AboutAppFragment();
                replace(aboutAppFragment, getActivity().getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                break;
            case R.id.fragment_restaurant_more_btn_comment_rate:
                break;
            case R.id.fragment_restaurant_more_btn_change_pass:
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                replace(changePasswordFragment, getActivity().getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                break;
            case R.id.fragment_restaurant_more_btn_log_out:
                FragmentManager manager = getFragmentManager();
                LogOutDialog logOutDialog = new LogOutDialog();
                logOutDialog.show(manager, "signup dialog");
                break;
        }
    }
}

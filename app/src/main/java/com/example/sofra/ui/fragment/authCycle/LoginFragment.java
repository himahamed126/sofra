package com.example.sofra.ui.fragment.authCycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.activity.HomeActivity;
import com.example.sofra.ui.activity.SplashActivity;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SharedPreferencesManger.setSharedPreferences;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.CITY_ID;
import static com.example.sofra.data.local.SofraConstans.CITY_NAME;
import static com.example.sofra.data.local.SofraConstans.EMAIL;
import static com.example.sofra.data.local.SofraConstans.NAME;
import static com.example.sofra.data.local.SofraConstans.PASSWORD;
import static com.example.sofra.data.local.SofraConstans.PHONE;
import static com.example.sofra.data.local.SofraConstans.PROFILE_IMAGE;
import static com.example.sofra.data.local.SofraConstans.REGION_ID;
import static com.example.sofra.data.local.SofraConstans.REGION_NAME;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.REST_AVAILABILITY;
import static com.example.sofra.data.local.SofraConstans.REST_CITY_ID;
import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYCOAST;
import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYTIME;
import static com.example.sofra.data.local.SofraConstans.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstans.REST_ID;
import static com.example.sofra.data.local.SofraConstans.REST_IMAGE;
import static com.example.sofra.data.local.SofraConstans.REST_MINIUMCHARGE;
import static com.example.sofra.data.local.SofraConstans.REST_NAME;
import static com.example.sofra.data.local.SofraConstans.REST_PASSWORD;
import static com.example.sofra.data.local.SofraConstans.REST_PHONE;
import static com.example.sofra.data.local.SofraConstans.REST_REGION_ID;
import static com.example.sofra.data.local.SofraConstans.REST_WHATSAPP;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.replace;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.fragment_login_ed_email)
    EditText fragmentLoginEdEmail;
    @BindView(R.id.fragment_login_ed_password)
    EditText fragmentLoginEdPassword;

    private RegisterClientFragment registerClientFragment;
    private RegisterRestaurantStep1Fragment registerRestaurantStep1Fragment;
    private ForgottenPasswordFragment forgottenPasswordFragment;

    private Unbinder unbinder;
    private ApiServices apiServices;
    private String TAG = "login";
    private String userType;
    private String email, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_login, container, false);
        setUpActivity();
        baseActivity.baseFragment = this;
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        setSharedPreferences(getActivity());
        userType = LoadData(getActivity(), USER_TYPE);

        registerClientFragment = new RegisterClientFragment();
        registerRestaurantStep1Fragment = new RegisterRestaurantStep1Fragment();
        forgottenPasswordFragment = new ForgottenPasswordFragment();

        return view;
    }

    @OnClick({R.id.fragment_login_tv_dont_have_acc, R.id.fragment_login_tv_forget_password, R.id.fragment_login_btn_login})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_login_tv_dont_have_acc:
                if (userType.equals("client")) {
                    replace(registerClientFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
                } else {
                    replace(registerRestaurantStep1Fragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
                }
                break;
            case R.id.fragment_login_tv_forget_password:
                replace(forgottenPasswordFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
                break;
            case R.id.fragment_login_btn_login:
                email = fragmentLoginEdEmail.getText().toString().trim();
                password = fragmentLoginEdPassword.getText().toString().trim();
                if (LoadData(getActivity(), USER_TYPE).equals("client")) {
                    loginClient();
                } else if (LoadData(getActivity(), USER_TYPE).equals("restaurant")) {
                    loginRestaurant();
                }
        }
    }

    private void loginClient() {
        apiServices.getLoginClient(email, password).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                        Log.i(TAG, response.body().getData().getApiToken());
                        SaveData(getActivity(), API_TOKEN, response.body().getData().getApiToken());
                        SaveData(getActivity(), NAME, response.body().getData().getUser().getName());
                        SaveData(getActivity(), EMAIL, response.body().getData().getUser().getEmail());
                        SaveData(getActivity(), PASSWORD, password);
                        SaveData(getActivity(), PHONE, response.body().getData().getUser().getPhone());
                        SaveData(getActivity(), CITY_NAME, response.body().getData().getUser().getRegion().getCity().getName());
                        SaveData(getActivity(), CITY_ID, response.body().getData().getUser().getRegion().getCityId());
                        SaveData(getActivity(), REGION_NAME, response.body().getData().getUser().getRegion().getName());
                        SaveData(getActivity(), REGION_ID, response.body().getData().getUser().getRegion().getId());
                        SaveData(getActivity(), PROFILE_IMAGE, response.body().getData().getUser().getPhotoUrl());
                        SaveData(getActivity(), REST_ID, response.body().getData().getUser().getId());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i(TAG, response.body().getMsg());
                        customToast(getActivity(), response.body().getMsg(), true);
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void loginRestaurant() {
        apiServices.getLoginRestaurant(email, password).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                        SaveData(getActivity(), REST_API_TOKEN, response.body().getData().getApiToken());
                        SaveData(getActivity(), REST_NAME, response.body().getData().getUser().getName());
                        SaveData(getActivity(), REST_EMAIL, response.body().getData().getUser().getEmail());
                        SaveData(getActivity(), REST_PASSWORD, password);
                        SaveData(getActivity(), REST_WHATSAPP, response.body().getData().getUser().getWhatsapp());
                        SaveData(getActivity(), REST_DELIVERYCOAST, response.body().getData().getUser().getDeliveryCost());
                        SaveData(getActivity(), REST_DELIVERYTIME, response.body().getData().getUser().getDeliveryTime());
                        SaveData(getActivity(), REST_IMAGE, response.body().getData().getUser().getPhotoUrl());
                        SaveData(getActivity(), REST_PHONE, response.body().getData().getUser().getPhone());
                        SaveData(getActivity(), REST_MINIUMCHARGE, response.body().getData().getUser().getMinimumCharger());
                        SaveData(getActivity(), REST_AVAILABILITY, response.body().getData().getUser().getAvailability());
                        SaveData(getActivity(), REST_REGION_ID, response.body().getData().getUser().getRegionId());
                        SaveData(getActivity(), REST_CITY_ID, response.body().getData().getUser().getRegion().getCityId());

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i(TAG, response.body().getMsg());
                        customToast(getActivity(), response.body().getMsg(), true);
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBack() {
        Intent intent = new Intent(getActivity(), SplashActivity.class);
        startActivity(intent);
    }
}

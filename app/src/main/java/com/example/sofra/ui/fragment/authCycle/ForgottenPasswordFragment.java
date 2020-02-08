package com.example.sofra.ui.fragment.authCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.*;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ForgottenPasswordFragment extends BaseFragment {
    @BindView(R.id.fragment_forgotten_password_ed_email)
    EditText fragmentForgottenPasswordEdEmail;
    Unbinder unbinder;

    private ApiServices apiServices;
    private RestPasswordFragment restPasswordFragment;
    private String TAG = "forget password";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_forgotten_password, container, false);
        setUpActivity();
        unbinder = ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        restPasswordFragment = new RestPasswordFragment();

        return view;
    }

    private void forgetPasswordClient() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.restPasswordClient(fragmentForgottenPasswordEdEmail.getText().toString()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                    replace(restPasswordFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    private void forgetPasswordRestaurant() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.restPasswordRestaurant(fragmentForgottenPasswordEdEmail.getText().toString()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                    replace(restPasswordFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.fragment_forgotten_password_btn_send)
    void onViewClicked() {
        if (SharedPreferencesManger.LoadData(getActivity(), USER_TYPE).equals("client")) {
            forgetPasswordClient();
        } else {
            forgetPasswordRestaurant();
        }
    }

    @Override
    public void onBack() {
        LoginFragment loginFragment = new LoginFragment();
        replace(loginFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }
}

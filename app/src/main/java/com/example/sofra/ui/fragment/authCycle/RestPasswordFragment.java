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
import static com.example.sofra.data.local.SharedPreferencesManger.*;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestPasswordFragment extends BaseFragment {
    @BindView(R.id.fragment_rest_password_ed_code)
    EditText fragmentRestPasswordEdCode;
    @BindView(R.id.fragment_rest_password_ed_password)
    EditText fragmentRestPasswordEdPassword;
    @BindView(R.id.fragment_rest_password_ed_confirm_password)
    EditText fragmentRestPasswordEdConfirmPassword;
    private Unbinder unbinder;

    private ApiServices apiServices;
    private String TAG = "new password";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_auth_rest_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        return view;
    }

    private void newPasswordClient() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.newPasswordClient(fragmentRestPasswordEdCode.getText().toString(),
                fragmentRestPasswordEdPassword.getText().toString(), fragmentRestPasswordEdConfirmPassword.getText().toString())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            customToast(getActivity(), response.body().getMsg(), false);
                        } else {
                            Log.i(TAG, response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });

    }

    private void newPasswordRestaurant() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.newPasswordRestaurant(fragmentRestPasswordEdCode.getText().toString(),
                fragmentRestPasswordEdPassword.getText().toString(), fragmentRestPasswordEdConfirmPassword.getText().toString())
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            customToast(getActivity(), response.body().getMsg(), false);
                        } else {
                            Log.i(TAG, response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });

    }

    @OnClick(R.id.fragment_rest_password_btn_send)
    public void onViewClicked() {
        newPasswordClient();
        if (LoadData(getActivity(), USER_TYPE).equals("client")) {
            newPasswordClient();
        } else {
            newPasswordRestaurant();
        }
    }

    @Override
    public void onBack() {
        ForgottenPasswordFragment forgottenPasswordFragment = new ForgottenPasswordFragment();
        HelperMethod.replace(forgottenPasswordFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }
}

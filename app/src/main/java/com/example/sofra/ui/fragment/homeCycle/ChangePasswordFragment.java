package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.REST_PASSWORD;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.fragment_change_password_ed_old_password)
    EditText fragmentChangePasswordEdOldPassword;
    @BindView(R.id.fragment_change_password_ed_new_password)
    EditText fragmentChangePasswordEdNewPassword;
    @BindView(R.id.fragment_change_password_ed_confirm_new_password)
    EditText fragmentChangePasswordEdConfirmNewPassword;

    ApiServices apiServices;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        return view;
    }

    private void changeClientPassword() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.changeRestaurantPassword(LoadData(getActivity(), REST_API_TOKEN),
                fragmentChangePasswordEdOldPassword.getText().toString(),
                fragmentChangePasswordEdNewPassword.getText().toString(),
                fragmentChangePasswordEdConfirmNewPassword.getText().toString()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                    SaveData(getActivity(), REST_PASSWORD, fragmentChangePasswordEdConfirmNewPassword.getText().toString());
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void changeRestaurantPassword() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.changeRestaurantPassword(LoadData(getActivity(), REST_API_TOKEN),
                fragmentChangePasswordEdOldPassword.getText().toString(),
                fragmentChangePasswordEdNewPassword.getText().toString(),
                fragmentChangePasswordEdConfirmNewPassword.getText().toString()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                    SaveData(getActivity(), REST_PASSWORD, fragmentChangePasswordEdConfirmNewPassword.getText().toString());
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_create_restaurant_offer_btn_add)
    public void onViewClicked() {
        if (LoadData(getActivity(), USER_TYPE).equals("client")) {
            changeClientPassword();
        } else {
            changeRestaurantPassword();
        }

    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

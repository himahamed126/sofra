package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.commission.Commission;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class CommissionFragment extends BaseFragment {
    @BindView(R.id.fragment_commission_tv_total)
    TextView fragmentCommissionTvTotal;
    @BindView(R.id.fragment_commission_tv_commission)
    TextView fragmentCommissionTvCommission;
    @BindView(R.id.fragment_commission_tv_paid)
    TextView fragmentCommissionTvPaid;
    @BindView(R.id.fragment_commission_tv_residual)
    TextView fragmentCommissionTvResidual;

    ApiServices apiServices;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_commission, container, false);

        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        getCommission();

        return view;
    }

    private void getCommission() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getCommission(LoadData(getActivity(), REST_API_TOKEN)).enqueue(new Callback<Commission>() {
            @Override
            public void onResponse(Call<Commission> call, Response<Commission> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    fragmentCommissionTvTotal.setText(String.valueOf(response.body().getData().getTotal()));
                    fragmentCommissionTvCommission.setText(String.valueOf(response.body().getData().getCommission()));
                    fragmentCommissionTvPaid.setText(String.valueOf(response.body().getData().getPayments()));
                    fragmentCommissionTvResidual.setText(String.valueOf(response.body().getData().getNetCommissions()));
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                }
            }

            @Override
            public void onFailure(Call<Commission> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

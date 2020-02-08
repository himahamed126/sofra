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
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ViewOfferDetailsFragment extends BaseFragment {
    @BindView(R.id.fragment_view_offer_details_tv_offer_name)
    TextView fragmentViewOfferDetailsTvOfferName;
    @BindView(R.id.fragment_view_offer_details_tv_offer_details)
    TextView fragmentViewOfferDetailsTvOfferDetails;
    @BindView(R.id.fragment_view_offer_details_tv_offer_start)
    TextView fragmentViewOfferDetailsTvOfferStart;
    @BindView(R.id.fragment_view_offer_details_tv_offer_end)
    TextView fragmentViewOfferDetailsTvOfferEnd;

    ApiServices apiServices;
    public OfferData offer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_view_offer_details, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        setData();
        return view;
    }

    private void setData() {
        fragmentViewOfferDetailsTvOfferName.setText(offer.getName());
        fragmentViewOfferDetailsTvOfferDetails.setText(offer.getDescription());
        fragmentViewOfferDetailsTvOfferStart.setText(offer.getStartingAt());
        fragmentViewOfferDetailsTvOfferEnd.setText(offer.getEndingAt());
    }

    private void getOfferDetails(int offerId) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getOffersDetails(offerId).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_view_offer_details_btn_get_offer)
    public void onViewClicked() {
        getOfferDetails(offer.getId());
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

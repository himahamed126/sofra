package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.OffersAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class OfferListFragment extends BaseFragment {
    @BindView(R.id.fragment_offer_list_rv)
    RecyclerView fragmentOfferListRv;

    private ApiServices apiServices;
    private List<OfferData> orderDataList;
    private OffersAdapter offersAdapter;
    private String TAG = "client order pending";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_offer_list, container, false);
        ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        getOffers(1);
        initRecyc();
        return view;
    }

    private void initRecyc() {
        orderDataList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        offersAdapter = new OffersAdapter(getActivity(), orderDataList);
        fragmentOfferListRv.setLayoutManager(linearLayoutManager);
        fragmentOfferListRv.setAdapter(offersAdapter);
    }

    private void getOffers(int restaurantId) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getOffers(restaurantId).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        orderDataList.addAll(response.body().getData().getData());
                        offersAdapter.notifyDataSetChanged();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (
                        Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

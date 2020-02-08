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
import com.example.sofra.adapter.RestaurantOffersAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.*;

public class RestaurantOfferListFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_list_rv_offers)
    RecyclerView fragmentRestaurantListRvOffers;

    private ApiServices apiServices;
    private RestaurantOffersAdapter restaurantOffersAdapter;
    private List<OfferData> offerDataList;
    private String TAG = "restaurant offer list";
    private OnEndLess onEndLess;
    private int maxPage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_offer_list, container, false);
        ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        getMyOffers(1);
        initRec();
        return view;
    }

    private void initRec() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentRestaurantListRvOffers.setLayoutManager(linearLayoutManager);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page < maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        getMyOffers(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
    }

    private void getMyOffers(int page) {
        offerDataList = new ArrayList<>();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getMyOffers(SharedPreferencesManger.LoadData(getActivity(), REST_API_TOKEN), page).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    offerDataList.addAll(response.body().getData().getData());
                    maxPage = response.body().getData().getLastPage();
                    restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), offerDataList);
                    fragmentRestaurantListRvOffers.setAdapter(restaurantOffersAdapter);
                } else {
                    Log.i(TAG, response.body().getMsg());
                }

            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_restaurant_list_btn_add_offer)
    public void onViewClicked() {
        CreateRestaurantOfferFragment createRestaurantOfferFragment = new CreateRestaurantOfferFragment();
        replace(createRestaurantOfferFragment, getFragmentManager(), R.id.activity_home_fl_frame, null, null);
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.data.model.restaurant.RestaurantDetails;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.*;

public class RestaurantInfoFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_info_tv_available)
    TextView fragmentRestaurantInfoTvAvailable;
    @BindView(R.id.fragment_restaurant_info_tv_city)
    TextView fragmentRestaurantInfoTvCity;
    @BindView(R.id.fragment_restaurant_info_tv_region)
    TextView fragmentRestaurantInfoTvRegion;
    @BindView(R.id.fragment_restaurant_info_tv_minim_charge)
    TextView fragmentRestaurantInfoTvMinimCharge;
    @BindView(R.id.fragment_restaurant_info_tv_delivery_charge)
    TextView fragmentRestaurantInfoTvDeliveryCharge;

    private ApiServices apiServices;
    Restaurant restaurantData;
    private String TAG = "errorrrrrrrrrrrrr";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        setData();
        return view;
    }

    private void setData() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getRestaurantDetails(restaurantData.getId()).enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        fragmentRestaurantInfoTvAvailable.setText(restaurantData.getAvailability());
                        fragmentRestaurantInfoTvCity.setText(response.body().getData().getRegion().getCity().getName());
                        fragmentRestaurantInfoTvRegion.setText(response.body().getData().getRegion().getName());
                        fragmentRestaurantInfoTvMinimCharge.setText(restaurantData.getMinimumCharger());
                        fragmentRestaurantInfoTvDeliveryCharge.setText(restaurantData.getDeliveryCost());
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.adapter.RestaurantListAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.publicData.RegionData;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.data.model.restaurant.Restaurants;
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
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestaurantListFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_list_rv_restaurants)
    RecyclerView homeRv;
    @BindView(R.id.fragment_restaurant_list_sp_city)
    AppCompatSpinner spCity;
    @BindView(R.id.fragment_restaurant_list_et_restaurant)
    AppCompatEditText searchEd;

    private ApiServices apiServices;
    private List<Restaurant> restaurantList;
    private RestaurantListAdapter adapter;
    private String TAG = "Error";
    private LinearLayoutManager layoutManager;

    private OnEndLess onEndLess;
    private int MaxPage;

    private GeneralResponseAdapter generalAdapter;
    private ArrayList<RegionData> city;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        restaurantList = new ArrayList<>();
        adapter = new RestaurantListAdapter(getActivity(), restaurantList);
        layoutManager = new LinearLayoutManager(getActivity());
        homeRv.setLayoutManager(layoutManager);
        homeRv.setAdapter(adapter);

        getCities();
        generalAdapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.select_city));
        spCity.setAdapter(generalAdapter);
        getRestaurants(1);
        onEndLess = new OnEndLess(layoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= MaxPage) {
                    if (MaxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getRestaurants(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        homeRv.addOnScrollListener(onEndLess);
        return view;
    }

    private void getRestaurants(int page) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getAllRestaurants(page).enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    try {
                        restaurantList.addAll(response.body().getData().getData());
                        MaxPage = response.body().getData().getLastPage();
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }

                } else {
                    Log.i(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void getCities() {
        city = new ArrayList<>();
        apiServices.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        city.addAll(response.body().getData().getData());
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void getRestWithFilter(String keyWord, int regionId) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getRestWithFilter(keyWord, regionId).enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    restaurantList.clear();
                    restaurantList.addAll(response.body().getData().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_restaurant_list_iv_search_btn)
    public void onViewClicked() {
        getRestWithFilter(searchEd.getText().toString(), spCity.getSelectedItemPosition());
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

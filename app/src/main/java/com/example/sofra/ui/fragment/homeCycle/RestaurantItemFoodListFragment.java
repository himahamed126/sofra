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
import com.example.sofra.adapter.CategoryFilterAdapter;
import com.example.sofra.adapter.RestaurantItemAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.restaurant.Category;
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.data.model.foodlist.Foodlist;
import com.example.sofra.data.model.restaurant.Restaurant;
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

public class RestaurantItemFoodListFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_item_food_list_rv_items)
    RecyclerView foodListRv;
    @BindView(R.id.fragment_restaurant_item_food_list_rv_categories)
    RecyclerView categoriesRv;

    private ApiServices apiServices;

    private List<Item> foodList;
    private List<CategoryData> categoryDataList;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager categoryLayoutManager;
    private RestaurantItemAdapter itemAdapter;
    private CategoryFilterAdapter categoryAdapter;
    Restaurant restaurantData;

    private int categoryId = 0;
    private String TAG = "Food List";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_item_food_list, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        foodList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        itemAdapter = new RestaurantItemAdapter(getActivity(), foodList);
        foodListRv.setLayoutManager(linearLayoutManager);
        foodListRv.setAdapter(itemAdapter);

        categoryDataList = new ArrayList<>();
        categoryAdapter = new CategoryFilterAdapter(getActivity(), categoryDataList, restaurantData.getId(), RestaurantItemFoodListFragment.this);
        categoryLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        categoriesRv.setLayoutManager(categoryLayoutManager);
        categoriesRv.setAdapter(categoryAdapter);

        getCategories();
        getItems(restaurantData.getId(), categoryId);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getCategories() {
        categoryDataList.add(new CategoryData("All", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcThgwBsaHfG9JsLTxTWUboS18RYAa8W4xSBHwUx6QED-ytXC85e", 0));
        apiServices.getCategories(restaurantData.getId()).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.body().getStatus() == 1) {
                    categoryDataList.addAll(response.body().getData());
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });
    }

    public void getItems(int restaurantId, int categoryId) {
//        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getItems(restaurantId, categoryId).enqueue(new Callback<Foodlist>() {

            @Override
            public void onResponse(Call<Foodlist> call, Response<Foodlist> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        foodList.clear();
                        foodList.addAll(response.body().getData().getData());
                        itemAdapter = new RestaurantItemAdapter(getActivity(), foodList);
                        foodListRv.setAdapter(itemAdapter);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Foodlist> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

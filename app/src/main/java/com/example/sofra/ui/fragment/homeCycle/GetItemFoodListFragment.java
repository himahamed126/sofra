package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.ItemFoodAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.foodlist.Foodlist;
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.replace;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class GetItemFoodListFragment extends BaseFragment {
    @BindView(R.id.fragment_get_item_list_food_rv_food_list)
    RecyclerView fragmentItemFoodListRv;
    @BindView(R.id.fragment_get_item_list_food_tv_title)
    TextView fragmentGetItemListFoodTvTitle;

    private ApiServices apiServices;

    private ItemFoodAdapter itemFoodAdapter;
    private ArrayList<Item> foodItemsList = new ArrayList<>();
    private Unbinder unbinder;

    public CategoryData categoryData;

    private static final String TAG = "GetItemFoodListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_get_item_list_food, container, false);

        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        fragmentGetItemListFoodTvTitle.setText(categoryData.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        foodItemsList = new ArrayList<>();
        fragmentItemFoodListRv.setLayoutManager(layoutManager);

        Log.i(TAG, LoadData(getActivity(), REST_API_TOKEN));
        Log.i(TAG, String.valueOf(categoryData.getId()));

        getFoodItem();

        return view;
    }


    private void getFoodItem() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getAllFoodItem(LoadData(getActivity(), REST_API_TOKEN), categoryData.getId()).enqueue(new Callback<Foodlist>() {
            @Override
            public void onResponse(Call<Foodlist> call, Response<Foodlist> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    foodItemsList.addAll(response.body().getData().getData());
                    itemFoodAdapter = new ItemFoodAdapter(getActivity(), foodItemsList);
                    fragmentItemFoodListRv.setAdapter(itemFoodAdapter);
                } else {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Foodlist> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_get_item_list_food_fab_add_item)
    public void onViewClicked() {
        CreateItemFoodFragment createItemFoodFragment = new CreateItemFoodFragment();
        createItemFoodFragment.categoryData = categoryData;
        replace(createItemFoodFragment, getActivity().getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

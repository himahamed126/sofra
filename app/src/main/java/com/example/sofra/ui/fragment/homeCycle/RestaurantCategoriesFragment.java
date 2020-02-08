package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.CategoryAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.dialog.CategoryDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import static com.example.sofra.data.local.SofraConstans.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstans.REST_PASSWORD;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestaurantCategoriesFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_categories_rv)
    RecyclerView fragmentRestaurantCategoriesRv;
    @BindView(R.id.fragment_restaurant_categories_flb_add_category)
    FloatingActionButton fragmentRestaurantCategoriesFlbAddCategory;

    private ApiServices apiServices;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter categoryAdapter;
    private ArrayList<CategoryData> categoryDataList = new ArrayList<>();
    private Unbinder unbinder;
    private String TAG = "restaurant category";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupHomeActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_categories, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        layoutManager = new LinearLayoutManager(getActivity());
        fragmentRestaurantCategoriesRv.setLayoutManager(layoutManager);

        getCategory();
        return view;
    }

    private void getCategory() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        categoryDataList = new ArrayList<>();
        apiServices.getLoginRestaurant(LoadData(getActivity(), REST_EMAIL), LoadData(getActivity(), REST_PASSWORD)).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    categoryDataList.addAll(response.body().getData().getUser().getCategories());
                    categoryAdapter = new CategoryAdapter(getActivity(), categoryDataList);
                    fragmentRestaurantCategoriesRv.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, t.getMessage());
            }
        });
    }


    @OnClick(R.id.fragment_restaurant_categories_flb_add_category)
    public void onViewClicked() {
        FragmentManager fragmentManager = getFragmentManager();
        CategoryDialog categoryDialog = new CategoryDialog();
        categoryDialog.show(fragmentManager, "add category");
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

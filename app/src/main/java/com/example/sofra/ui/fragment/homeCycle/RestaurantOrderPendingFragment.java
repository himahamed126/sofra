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
import com.example.sofra.adapter.RestaurantOrderAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.order.Order;
import com.example.sofra.data.model.order.OrderData;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestaurantOrderPendingFragment extends BaseFragment {
    @BindView(R.id.fragment_order_pending_rv_orders)
    RecyclerView fragmentOrderPendingRvOrders;

    private ApiServices apiServices;
    private List<OrderData> orderDataList;
    private RestaurantOrderAdapter restaurantOrderAdapter;
    private OnEndLess onEndLess;
    private int MaxPage;
    private String TAG = "restaurant order pending";
    private String api_token = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_order_pending, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        getNewOrder(LoadData(getActivity(), API_TOKEN), 1);
        initRecyc();
        return view;
    }

    private void initRecyc() {
        orderDataList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        restaurantOrderAdapter = new RestaurantOrderAdapter(getActivity(), orderDataList);
        fragmentOrderPendingRvOrders.setLayoutManager(linearLayoutManager);
        fragmentOrderPendingRvOrders.setAdapter(restaurantOrderAdapter);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= MaxPage) {
                    if (MaxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getNewOrder(LoadData(getActivity(), API_TOKEN), current_page);
                        restaurantOrderAdapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        fragmentOrderPendingRvOrders.addOnScrollListener(onEndLess);
    }

    private void getNewOrder(String apiToken, int page) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getRestaurantOrders(api_token, "pending", page).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        orderDataList.addAll(response.body().getData().getData());
                        MaxPage = response.body().getData().getLastPage();
                        restaurantOrderAdapter.notifyDataSetChanged();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (
                        Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

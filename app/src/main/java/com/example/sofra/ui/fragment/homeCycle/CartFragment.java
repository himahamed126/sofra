package com.example.sofra.ui.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.OrderAdapter;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.activity.AuthActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.replace;

public class CartFragment extends BaseFragment {
    @BindView(R.id.fragment_cart_rv_orders)
    RecyclerView rvOrders;
    @BindView(R.id.fragment_cart_tv_total)
    TextView fragmentCartTvTotal;

    public List<OrderItem> ordersList;
    private LinearLayoutManager linearLayoutManager;
    private OrderAdapter orderAdapter;
    private int total;
    private String TAG = "cart fragment";
    private String clientApi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);

        clientApi = LoadData(getActivity(), API_TOKEN);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        orderAdapter = new OrderAdapter(getActivity(), ordersList);
        rvOrders.setLayoutManager(linearLayoutManager);
        rvOrders.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        for (int i = 0; i < ordersList.size(); i++) {
            total = (int) (total + (ordersList.get(i).getQuantity() * ordersList.get(i).getPrice()));
        }
        fragmentCartTvTotal.setText(String.valueOf(total));
        return view;
    }

    @OnClick({R.id.fragment_cart_btn_confirm_request, R.id.fragment_cart_btn_add_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_cart_btn_confirm_request:
                if (clientApi == (null)) {
                    Intent intent = new Intent(getActivity(), AuthActivity.class);
                    startActivity(intent);
                } else {
                    CompleteOrderFragment completeOrderFragment = new CompleteOrderFragment();
                    completeOrderFragment.data = ordersList;
                    replace(completeOrderFragment, getActivity().getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }
                break;
            case R.id.fragment_cart_btn_add_more:
                super.onBack();
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

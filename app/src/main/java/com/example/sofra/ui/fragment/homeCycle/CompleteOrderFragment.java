package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.data.model.order.Order;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.NAME;
import static com.example.sofra.data.local.SofraConstans.PHONE;
import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYCOAST;
import static com.example.sofra.data.local.room.RoomManger.getInstance;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class CompleteOrderFragment extends BaseFragment {
    @BindView(R.id.fragment_complete_order_ed_note)
    EditText fragmentCompleteOrderEdNote;
    @BindView(R.id.fragment_complete_order_ed_address)
    EditText fragmentCompleteOrderEdAddress;
    @BindView(R.id.fragment_complete_order_tv_total)
    TextView fragmentCompleteOrderTvTotal;
    @BindView(R.id.fragment_complete_order_tv_delivery_coast)
    TextView fragmentCompleteOrderTvDeliveryCoast;
    @BindView(R.id.fragment_complete_order_tv_total_amount)
    TextView fragmentCompleteOrderTvTotalAmount;
    @BindView(R.id.fragment_complete_order_rg_pay_method)
    RadioGroup rg;

    private ApiServices apiServices;
    private RoomDao roomDao;
    private String apiToken, name, deliveryCoast;
    private int paymentMethod, phone;
    private double total, totalAmount;
    public List<OrderItem> data;
    private List<Integer> items = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    private String TAG = "complete order";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_complete_order, container, false);
        ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        roomDao = getInstance(getActivity()).roomDao();

        setData();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_complete_order_rb_cash:
                        paymentMethod = 1;
                        break;
                    case R.id.fragment_complete_order_rb_online:
                        paymentMethod = 2;
                        break;
                }
            }
        });

        return view;
    }

    private void setData() {
        try {
            apiToken = LoadData(getActivity(), API_TOKEN);
            phone = Integer.parseInt(LoadData(getActivity(), PHONE));
            name = LoadData(getActivity(), NAME);
            deliveryCoast = (LoadData(getActivity(), REST_DELIVERYCOAST));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        for (int i = 0; i < data.size(); i++) {
            total = data.get(i).getQuantity() * data.get(i).getPrice();
            quantities.add(data.get(i).getQuantity());
            notes.add(data.get(i).getNote());
            items.add(data.get(i).getItem_id());
        }

        fragmentCompleteOrderTvDeliveryCoast.setText(deliveryCoast);
        fragmentCompleteOrderTvTotal.setText(String.valueOf(total));

        totalAmount = total + Double.parseDouble(deliveryCoast);
        fragmentCompleteOrderTvTotalAmount.setText(String.valueOf(totalAmount));
    }

    private void confirmRequest() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.addNewOrder(data.get(0).getRestaurant_id(), fragmentCompleteOrderEdNote.getText().toString(),
                fragmentCompleteOrderEdAddress.getText().toString(), paymentMethod, phone, name,
                apiToken, items, quantities, notes).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                    } else {
                        customToast(getActivity(), response.body().getMsg(), false);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void deleteAll() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                roomDao.deleteAll();
            }
        });
    }

    @OnClick(R.id.fragment_complete_order_btn_confirm_request)
    public void onViewClicked() {
        confirmRequest();
        deleteAll();
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

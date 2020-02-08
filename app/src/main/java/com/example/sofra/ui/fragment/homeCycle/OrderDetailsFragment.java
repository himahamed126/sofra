package com.example.sofra.ui.fragment.homeCycle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.OrderDetailAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.OrderDetail;
import com.example.sofra.data.model.order.OrderData;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.dialog.RejectDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.acceptOrder;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class OrderDetailsFragment extends BaseFragment {
    @BindView(R.id.fragment_order_details_iv_rest_image)
    CircleImageView fragmentOrderDetailsIvRestImage;
    @BindView(R.id.fragment_order_details_tv_name)
    TextView fragmentOrderDetailsTvName;
    @BindView(R.id.fragment_order_details_tv_date)
    TextView fragmentOrderDetailsTvDate;
    @BindView(R.id.fragment_order_details_tv_address)
    TextView fragmentOrderDetailsTvAddress;
    @BindView(R.id.fragment_order_details_rv_items)
    RecyclerView fragmentOrderDetailsRvItems;
    @BindView(R.id.fragment_order_details_tv_request_price)
    TextView fragmentOrderDetailsTvRequestPrice;
    @BindView(R.id.fragment_order_details_tv_delivery_coast)
    TextView fragmentOrderDetailsTvDeliveryCoast;
    @BindView(R.id.fragment_order_details_tv_total_amount)
    TextView fragmentOrderDetailsTvTotalAmount;
    @BindView(R.id.fragment_order_details_tv_payment)
    TextView fragmentOrderDetailsTvPayment;

    private ApiServices apiServices;
    public OrderData order;
    private double orderCoast = 0;
    private OrderDetailAdapter orderDetailAdapter;
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private String apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
    private String TAG = "order Detail fragment";
    private int REQUEST_PHONE_CALL = 123;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        setDate();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        fragmentOrderDetailsRvItems.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void setDate() {
        onLoadImageFromUrl(fragmentOrderDetailsIvRestImage, order.getItems().get(0).getPhotoUrl(), getActivity());
        fragmentOrderDetailsTvName.setText(order.getClient().getName());
        fragmentOrderDetailsTvDate.setText(order.getCreatedAt());
        fragmentOrderDetailsTvDeliveryCoast.setText(order.getDeliveryCost());
        fragmentOrderDetailsTvAddress.setText(order.getAddress());

        if (order.getPaymentMethodId() == "1") {
            fragmentOrderDetailsTvPayment.setText(getString(R.string.cash_payment));
        } else {
            fragmentOrderDetailsTvPayment.setText(getString(R.string.online_payment));
        }

        for (int i = 0; i < order.getItems().size(); i++) {
            orderCoast += Double.parseDouble(order.getItems().get(i).getPrice());
            orderDetails.add(new OrderDetail(order.getItems().get(i).getName(), order.getItems().get(i).getPrice()));
            orderDetailAdapter = new OrderDetailAdapter(getActivity(), orderDetails);
            fragmentOrderDetailsRvItems.setAdapter(orderDetailAdapter);
        }

        fragmentOrderDetailsTvRequestPrice.setText(order.getCost());
        double total = orderCoast + Double.parseDouble(order.getDeliveryCost());
        fragmentOrderDetailsTvTotalAmount.setText(String.valueOf(total));

    }

    @OnClick({R.id.fragment_order_details_btn_call, R.id.fragment_order_details_btn_accept, R.id.fragment_order_details_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_order_details_btn_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + order.getClient().getPhone()));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.fragment_order_details_btn_accept:
                acceptOrder(apiServices, apiToken, order.getId(), getActivity(), TAG);
                break;
            case R.id.fragment_order_details_btn_cancel:
                FragmentManager fragmentManager = getFragmentManager();
                RejectDialog rejectDialog = new RejectDialog();
                rejectDialog.id = order.getId();
                rejectDialog.show(fragmentManager, "reject dialog");
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

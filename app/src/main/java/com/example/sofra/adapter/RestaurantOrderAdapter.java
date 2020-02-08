package com.example.sofra.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.order.OrderData;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.ui.dialog.RejectDialog;
import com.example.sofra.ui.fragment.homeCycle.OrderDetailsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.acceptOrder;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.replace;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestaurantOrderAdapter extends RecyclerView.Adapter<RestaurantOrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderData> orderList;
    private ApiServices apiServices;
    private String TAG = "restaurant order adapter";
    private String apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
    String loadApiToken = LoadData(((AppCompatActivity) context), API_TOKEN);
    private int REQUEST_PHONE_CALL = 123;

    public RestaurantOrderAdapter(Context context, List orderList) {
        this.context = context;
        this.orderList = orderList;
        apiServices = getClient().create(ApiServices.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OrderData order = orderList.get(position);

        onLoadImageFromUrl(holder.itemRestaurantOrderIvImage, order.getClient().getPhotoUrl(), context);
        holder.itemRestaurantOrderTvName.setText(order.getClient().getName());
        holder.itemRestaurantOrderTvAddress.setText(order.getAddress());
        holder.itemRestaurantOrderTvTotal.setText(String.valueOf(order.getTotal()));
        for (int i = 0; i < order.getItems().size(); i++) {
            holder.itemRestaurantOrderTvRequestNum.setText(String.valueOf(order.getItems().get(i).getPivot().getOrderId()));
        }

        switch (order.getState()) {
            case "pending":
                holder.itemRestaurantOrderPendingBtnLy.setVisibility(View.VISIBLE);
                break;
            case "accepted":
                holder.itemRestaurantOrderCurrentBtnLy.setVisibility(View.VISIBLE);
                holder.itemRestaurantOrderCurrentTvCall.setText(order.getClient().getPhone());
                break;
            case "delivered":
                holder.itemRestaurantOrderCompleteBtnLy.setVisibility(View.VISIBLE);
                holder.itemRestaurantOrderTvComplete.setBackgroundResource(R.drawable.sh_green_btn);
                holder.itemRestaurantOrderTvComplete.setText(context.getString(R.string.completed_request));
                break;
            case "rejected":
            case "declined":
                holder.itemRestaurantOrderCompleteBtnLy.setVisibility(View.VISIBLE);
                holder.itemRestaurantOrderTvComplete.setBackgroundResource(R.drawable.sh_red_btn);
                holder.itemRestaurantOrderTvComplete.setText(context.getString(R.string.canceled_request));
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {
        OrderData order = orderList.get(position);
        //pending
        holder.itemRestaurantOrderPendingBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + order.getClient().getPhone()));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((AppCompatActivity) context), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    context.startActivity(intent);
                }
            }
        });
        holder.itemRestaurantOrderPendingBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder(apiServices, apiToken, order.getId(), ((AppCompatActivity) context), TAG);
                orderList.remove(position);
            }
        });
        holder.itemRestaurantOrderPendingBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                RejectDialog rejectDialog = new RejectDialog();
                rejectDialog.id = order.getId();
                rejectDialog.show(fragmentManager, "reject order");
//                orderList.remove(position);
            }
        });
        // current
        holder.itemRestaurantOrderCurrentBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + order.getClient().getPhone()));
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((AppCompatActivity) context), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    context.startActivity(intent);
                }
            }
        });
        holder.itemRestaurantOrderCurrentBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrderDelivery(order.getId(), apiToken, position);
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                orderDetailsFragment.order = order;
                replace(orderDetailsFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
            }
        });
    }


    private void confirmOrderDelivery(int orderId, String apiToken, int position) {
        showProgressDialog(((AppCompatActivity) context), context.getString(R.string.please_wait));
        apiServices.confirmRestaurantOrderDelivery(orderId, apiToken).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(((AppCompatActivity) context), response.body().getMsg(), false);
                    orderList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_restaurant_order_iv_image)
        ImageView itemRestaurantOrderIvImage;
        @BindView(R.id.item_restaurant_order_tv_name)
        TextView itemRestaurantOrderTvName;
        @BindView(R.id.item_restaurant_order_tv_request_num)
        TextView itemRestaurantOrderTvRequestNum;
        @BindView(R.id.item_restaurant_order_tv_total)
        TextView itemRestaurantOrderTvTotal;
        @BindView(R.id.item_restaurant_order_tv_address)
        TextView itemRestaurantOrderTvAddress;

        //pending
        @BindView(R.id.item_restaurant_order_pending_btn_ly)
        LinearLayout itemRestaurantOrderPendingBtnLy;
        @BindView(R.id.item_restaurant_order_pending_btn_call)
        LinearLayout itemRestaurantOrderPendingBtnCall;
        @BindView(R.id.item_restaurant_order_pending_btn_accept)
        LinearLayout itemRestaurantOrderPendingBtnAccept;
        @BindView(R.id.item_restaurant_order_pending_btn_cancel)
        LinearLayout itemRestaurantOrderPendingBtnCancel;
        // current
        @BindView(R.id.item_restaurant_order_current_tv_call)
        TextView itemRestaurantOrderCurrentTvCall;
        @BindView(R.id.item_restaurant_order_current_btn_call)
        LinearLayout itemRestaurantOrderCurrentBtnCall;
        @BindView(R.id.item_restaurant_order_current_btn_accept)
        LinearLayout itemRestaurantOrderCurrentBtnAccept;
        @BindView(R.id.item_restaurant_order_current_btn_ly)
        LinearLayout itemRestaurantOrderCurrentBtnLy;
        //complete
        @BindView(R.id.item_restaurant_order_tv_complete)
        TextView itemRestaurantOrderTvComplete;
        @BindView(R.id.item_restaurant_order_complete_btn_ly)
        LinearLayout itemRestaurantOrderCompleteBtnLy;

        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

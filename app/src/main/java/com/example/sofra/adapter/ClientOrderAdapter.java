package com.example.sofra.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.order.Order;
import com.example.sofra.data.model.order.OrderData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ClientOrderAdapter extends RecyclerView.Adapter<ClientOrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderData> orderList;
    private ApiServices apiServices;
    private String TAG = "client order adapter";

    public ClientOrderAdapter(Context context, List orderList) {
        this.context = context;
        this.orderList = orderList;
        apiServices = getClient().create(ApiServices.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OrderData order = orderList.get(position);

        for (int i = 0; i < order.getItems().size(); i++) {
            onLoadImageFromUrl(holder.itemClientPendingOrderIvImage, order.getItems().get(i).getPhotoUrl(), context);
            holder.itemClientPendingOrderTvName.setText(order.getItems().get(i).getName());
            holder.itemClientPendingOrderTvRequestNum.setText(String.valueOf(order.getItems().get(i).getPivot().getOrderId()));
        }
        holder.itemClientPendingOrderTvAddress.setText(order.getAddress());
        holder.itemClientPendingOrderTvTotal.setText(String.valueOf(order.getTotal()));

        switch (order.getState()) {
            case "pending":
                holder.itemClientPendingOrderBtnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.sh_blue_btn));
                holder.itemClientPendingOrderTvToggleBtn.setText(context.getString(R.string.cancel));
                break;
            case "accepted":
                holder.itemClientPendingOrderBtnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.sh_green_btn));
                holder.itemClientPendingOrderIvBtn.setImageResource(R.drawable.album_ic_done_white);
                holder.itemClientPendingOrderTvToggleBtn.setText(context.getString(R.string.confirm_order));
                break;
            case "delivered":
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.itemClientPendingOrderBtnCancel.getLayoutParams();
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                holder.itemClientPendingOrderBtnCancel.setLayoutParams(params);
                holder.itemClientPendingOrderBtnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.sh_green_btn));
                holder.itemClientPendingOrderTvToggleBtn.setText(context.getString(R.string.completed_request));
                holder.itemClientPendingOrderIvBtn.setVisibility(View.GONE);
                break;
            case "declined":
            case "rejected":
                params = (RelativeLayout.LayoutParams) holder.itemClientPendingOrderBtnCancel.getLayoutParams();
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                holder.itemClientPendingOrderBtnCancel.setLayoutParams(params);
                holder.itemClientPendingOrderBtnCancel.setBackground(ContextCompat.getDrawable(context, R.drawable.sh_red_btn));
                holder.itemClientPendingOrderTvToggleBtn.setText(context.getString(R.string.canceled_request));
                holder.itemClientPendingOrderIvBtn.setVisibility(View.GONE);
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {
        OrderData order = orderList.get(position);
        holder.itemClientPendingOrderBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getState().equals("pending")) {
                    declineOrder(order.getId(), LoadData(((AppCompatActivity) context), API_TOKEN), position);
                } else if (order.getState().equals("accepted")) {
                    confirmOrder(order.getId(), LoadData(((AppCompatActivity) context), API_TOKEN), position);
                }
            }
        });
    }

    private void declineOrder(int orderId, String apiToken, int position) {
        showProgressDialog(((AppCompatActivity) context), context.getString(R.string.please_wait));
        apiServices.declineOrder(orderId, apiToken).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
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
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    private void confirmOrder(int orderId, String apiToken, int position) {
        showProgressDialog(((AppCompatActivity) context), context.getString(R.string.please_wait));
        apiServices.confirmOrder(orderId, apiToken).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
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
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_client_pending_order_iv_image)
        CircleImageView itemClientPendingOrderIvImage;
        @BindView(R.id.item_client_pending_order_tv_name)
        TextView itemClientPendingOrderTvName;
        @BindView(R.id.item_client_pending_order_tv_request_num)
        TextView itemClientPendingOrderTvRequestNum;
        @BindView(R.id.item_client_pending_order_tv_total)
        TextView itemClientPendingOrderTvTotal;
        @BindView(R.id.item_client_pending_order_tv_address)
        TextView itemClientPendingOrderTvAddress;
        @BindView(R.id.item_client_pending_order_btn_cancel)
        RelativeLayout itemClientPendingOrderBtnCancel;
        @BindView(R.id.item_client_pending_order_iv_btn)
        ImageView itemClientPendingOrderIvBtn;
        @BindView(R.id.item_client_pending_order_tv_btn)
        TextView itemClientPendingOrderTvToggleBtn;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

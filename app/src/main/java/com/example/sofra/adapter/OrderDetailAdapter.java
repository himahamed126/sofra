package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.OrderDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetailList;

    public OrderDetailAdapter(Context context, List orderDetailList) {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        holder.itemOrderDetailsTvName.setText(orderDetail.getName());
        holder.itemOrderDetailsTvPrice.setText(orderDetail.getPrice());
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_order_details_tv_name)
        TextView itemOrderDetailsTvName;
        @BindView(R.id.item_order_details_tv_price)
        TextView itemOrderDetailsTvPrice;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

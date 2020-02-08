package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.data.local.room.RoomManger.getInstance;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderItem> ordersList = new ArrayList<>();
    private int quantity;
    private RoomDao roomDao;

    public OrderAdapter(Context context, List<OrderItem> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
        roomDao = getInstance(context).roomDao();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OrderItem order = ordersList.get(position);
        onLoadImageFromUrl(holder.orderImage, order.getPhoto(), context);
        holder.orderName.setText(order.getItem_name());
        holder.orderPrice.setText(String.valueOf(order.getPrice()));
        holder.itemOrderTvOrderCount.setText(String.valueOf(order.getQuantity()));
    }

    private void setAction(ViewHolder holder, int position) {
        OrderItem order = ordersList.get(position);

        holder.itemOrderIvAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        quantity = order.getQuantity();
                        quantity += 1;
                        order.setQuantity(quantity);
                        roomDao.update(order);
                        holder.itemOrderTvOrderCount.setText(String.valueOf(order.getQuantity()));
                    }
                });
            }
        });
        holder.itemOrderIvRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        quantity = order.getQuantity();
                        if (quantity > 1) {
                            quantity = order.getQuantity();
                            quantity -= 1;
                            order.setQuantity(quantity);
                            roomDao.update(order);
                            holder.itemOrderTvOrderCount.setText(String.valueOf(order.getQuantity()));
                        }
                    }
                });
            }
        });
        holder.itemOrderIvCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        roomDao.removeItem(order);
                        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ordersList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_order_iv_order_image)
        ImageView orderImage;
        @BindView(R.id.item_order_tv_order_name)
        TextView orderName;
        @BindView(R.id.item_order_tv_order_price)
        TextView orderPrice;
        @BindView(R.id.item_order_iv_remove_btn)
        ImageView itemOrderIvRemoveBtn;
        @BindView(R.id.item_order_tv_order_count)
        TextView itemOrderTvOrderCount;
        @BindView(R.id.item_order_iv_add_btn)
        ImageView itemOrderIvAddBtn;
        @BindView(R.id.item_order_iv_cancel_btn)
        ImageView itemOrderIvCancelBtn;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

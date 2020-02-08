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
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.ui.fragment.homeCycle.AddOrderFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sofra.helper.HelperMethod.*;

public class RestaurantItemAdapter extends RecyclerView.Adapter<RestaurantItemAdapter.ViewHolder> {

    private Context context;
    private List<Item> foodListData;

    public RestaurantItemAdapter(Context context, List<Item> foodListData) {
        this.context = context;
        this.foodListData = foodListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        Item foodList = foodListData.get(position);
        holder.name.setText(foodList.getName());
        holder.details.setText(foodList.getDescription());
        holder.price.setText(foodList.getPrice());
        onLoadImageFromUrl(holder.image, foodList.getPhotoUrl(), context);
    }

    private void setAction(ViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrderFragment addOrderFragment = new AddOrderFragment();
                addOrderFragment.item = foodListData.get(position);
                replace(addOrderFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.fragment_restaurant_list_fl_frame, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodListData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fragment_order_iv_item_image)
        ImageView image;
        @BindView(R.id.fragment_order_tv_item_name)
        TextView name;
        @BindView(R.id.fragment_order_tv_item_details)
        TextView details;
        @BindView(R.id.fragment_order_tv_item_price)
        TextView price;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.fragment.homeCycle.RestaurantContainerFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYCOAST;
import static com.example.sofra.data.local.SofraConstans.REST_ID;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.helper.HelperMethod.replace;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.name.setText(restaurant.getName());
        holder.rating.setRating(restaurant.getRate());
        HelperMethod.onLoadImageFromUrl(holder.image, restaurant.getPhotoUrl(), context);
        holder.minimCharge.setText(restaurant.getMinimumCharger());
        holder.dilviryCost.setText(restaurant.getDeliveryCost());
        if (restaurant.getAvailability().equals("open")) {
            holder.available.setText("مفتوح");
        } else {
            holder.available.setText("مغلق");
        }
    }

    private void setAction(ViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Restaurant restaurant = restaurantList.get(position);
                RestaurantContainerFragment restaurantContainerFragment = new RestaurantContainerFragment();
                restaurantContainerFragment.data = restaurant;
                replace(restaurantContainerFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.fragment_restaurant_list_fl_frame, null, null);
                SaveData((AppCompatActivity) context, REST_ID, String.valueOf(restaurant.getId()));
                SaveData((AppCompatActivity) context, REST_DELIVERYCOAST, String.valueOf(restaurant.getDeliveryCost()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_restaurant_tv_restaurant_name)
        TextView name;
        @BindView(R.id.item_restaurant_tv_restaurant_available)
        TextView available;
        @BindView(R.id.item_restaurant_rb_restaurant_rating)
        RatingBar rating;
        @BindView(R.id.item_restaurant_tv_minim_charge)
        TextView minimCharge;
        @BindView(R.id.item_restaurant_tv_delviry_cost)
        TextView dilviryCost;
        @BindView(R.id.item_restaurant_iv_restaurant_img)
        CircleImageView image;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

package com.example.sofra.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.ui.fragment.homeCycle.CreateItemFoodFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.replace;

public class ItemFoodAdapter extends RecyclerView.Adapter<ItemFoodAdapter.ViewHolder> {
    private Context context;
    private List<Item> myItemsDataList = new ArrayList<>();
    private ApiServices apiServices;
    private String TAG = "item adapter";

    public ItemFoodAdapter(Context context, List<Item> myItemsDataList) {
        this.context = context;
        this.myItemsDataList = myItemsDataList;
        apiServices = getClient().create(ApiServices.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        Item item = myItemsDataList.get(position);
        onLoadImageFromUrl(holder.foodItemIvFoodImage, item.getPhotoUrl(), context);
        holder.foodItemTvFoodTitle.setText(item.getName());
        holder.foodItemTvFoodDescription.setText(item.getDescription());
        holder.foodItemTvPrice.setText(item.getPrice());
    }

    private void setAction(ViewHolder holder, int position) {
        Item item = myItemsDataList.get(position);
        holder.foodItemIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(item.getId(), position);
            }
        });
        holder.foodItemIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateItemFoodFragment createItemFoodFragment = new CreateItemFoodFragment();
                createItemFoodFragment.item = item;
                replace(createItemFoodFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
            }
        });
    }

    private void deleteItem(int itemId, int position) {
        apiServices.deleteItem(itemId, LoadData(((AppCompatActivity) context), REST_API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(((AppCompatActivity) context), response.body().getMsg(), false);
                        myItemsDataList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return myItemsDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.food_item_iv_food_image)
        ImageView foodItemIvFoodImage;
        @BindView(R.id.food_item_tv_food_title)
        TextView foodItemTvFoodTitle;
        @BindView(R.id.food_item_tv_food_description)
        TextView foodItemTvFoodDescription;
        @BindView(R.id.food_item_tv_price)
        TextView foodItemTvPrice;
        @BindView(R.id.food_item_iv_delete)
        ImageView foodItemIvDelete;
        @BindView(R.id.food_item_iv_edit)
        ImageView foodItemIvEdit;
        @BindView(R.id.food_item_sl_swipe1)
        SwipeRevealLayout foodItemSlSwipe1;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

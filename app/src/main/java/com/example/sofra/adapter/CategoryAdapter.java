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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.ui.dialog.CategoryDialog;
import com.example.sofra.ui.fragment.homeCycle.GetItemFoodListFragment;

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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryData> categoryDataList;
    private ApiServices apiServices;
    private String TAG = "category adapter";

    public CategoryAdapter(Context context, List<CategoryData> categoryDataList) {
        this.context = context;
        this.categoryDataList = categoryDataList;
        apiServices = getClient().create(ApiServices.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        CategoryData category = categoryDataList.get(position);
        onLoadImageFromUrl(holder.restaurantItemIvCategoryImage, category.getPhotoUrl(), context);
        holder.restaurantItemTvCategoryName.setText(category.getName());
    }

    private void setAction(ViewHolder holder, int position) {
        CategoryData category = categoryDataList.get(position);
        holder.restaurantItemIvCategoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetItemFoodListFragment getItemFoodListFragment = new GetItemFoodListFragment();
                getItemFoodListFragment.categoryData = category;
                replace(getItemFoodListFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
            }
        });
        holder.categoryItemIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                CategoryDialog categoryDialog = new CategoryDialog();
                categoryDialog.categoryData = category;
                categoryDialog.show(manager, "update dialog");
                notifyDataSetChanged();
            }
        });
        holder.categoryItemIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(category.getId(), position);
            }
        });
    }

    private void deleteCategory(int categoryId, int position) {
        apiServices.deleteCategory(LoadData(((AppCompatActivity) context), REST_API_TOKEN), categoryId).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(((AppCompatActivity) context), response.body().getMsg(), false);
                        categoryDataList.remove(position);
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
        return categoryDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_item_iv_category_image)
        ImageView restaurantItemIvCategoryImage;
        @BindView(R.id.category_item_tv_category_name)
        TextView restaurantItemTvCategoryName;
        @BindView(R.id.category_item_iv_delete)
        ImageView categoryItemIvDelete;
        @BindView(R.id.category_item_iv_edit)
        ImageView categoryItemIvEdit;
        @BindView(R.id.category_item_sl_swipe1)
        SwipeRevealLayout categoryItemSlSwipe1;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

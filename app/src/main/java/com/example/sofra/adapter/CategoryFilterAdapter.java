package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.ui.fragment.homeCycle.RestaurantItemFoodListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder> {
    private Context context;
    private List<CategoryData> categoryDataList;
    private int restId;
    public RestaurantItemFoodListFragment foodListFragment;

    public CategoryFilterAdapter(Context context, List<CategoryData> categoryDataList, int restId,
                                 RestaurantItemFoodListFragment foodListFragment) {
        this.context = context;
        this.categoryDataList = categoryDataList;
        this.restId = restId;
        this.foodListFragment = foodListFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        CategoryData categoryData = categoryDataList.get(position);
        holder.name.setText(categoryData.getName());
        onLoadImageFromUrl(holder.image, categoryData.getPhotoUrl(), context);
    }

    private void setAction(ViewHolder holder, int position) {
        CategoryData categoryData = categoryDataList.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListFragment.getItems(restId, categoryData.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_category_filter_iv_avatar)
        CircleImageView image;
        @BindView(R.id.item_category_filter_tv_name)
        TextView name;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

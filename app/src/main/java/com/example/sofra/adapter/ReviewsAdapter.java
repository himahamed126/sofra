package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.reviews.ReviewsData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private Context context;
    private List<ReviewsData> reviewsList;

    public ReviewsAdapter(Context context, List<ReviewsData> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        ReviewsData reviews = reviewsList.get(position);
        holder.name.setText(reviews.getClient().getName());
        holder.comment.setText(reviews.getComment());
        switch (reviews.getRate()) {
            case "1":
                holder.image.setImageResource(R.drawable.ic_love);
                break;
            case "2":
                holder.image.setImageResource(R.drawable.ic_happy);
                break;
            case "3":
                holder.image.setImageResource(R.drawable.ic_smile);
                break;
            case "4":
                holder.image.setImageResource(R.drawable.ic_sad);
                break;
            case "5":
                holder.image.setImageResource(R.drawable.ic_angry);
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviews_item_iv_emojy)
        ImageView image;
        @BindView(R.id.reviews_item_tv_name)
        TextView name;
        @BindView(R.id.reviews_item_tv_review)
        TextView comment;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
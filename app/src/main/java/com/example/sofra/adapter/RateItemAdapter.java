package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.Emoji;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateItemAdapter extends RecyclerView.Adapter<RateItemAdapter.ViewHolder> {
    private Context context;
    private List<Emoji> emojiList;

    public RateItemAdapter(Context context, List emojiList) {
        this.context = context;
        this.emojiList = emojiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emojy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        Emoji emoji = emojiList.get(position);
        holder.image.setImageResource(emoji.getImage());
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_review_dialog_iv_1)
        ImageView image;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

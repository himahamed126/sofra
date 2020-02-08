package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.notification.Notification;
import com.example.sofra.data.model.notification.NotificationData;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<NotificationData> notificationsList;
    Date date;

    public NotificationAdapter(Context context, List notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        NotificationData notification = notificationsList.get(position);
        holder.itemNotificationTvNotificationText.setText(notification.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            date = sdf.parse(notification.getCreatedAt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime();
        String timeAgo = prettyTime.format(date);
        holder.itemNotificationTvTime.setText(timeAgo);
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_notification_tv_notification_text)
        TextView itemNotificationTvNotificationText;
        @BindView(R.id.item_notification_tv_time)
        TextView itemNotificationTvTime;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

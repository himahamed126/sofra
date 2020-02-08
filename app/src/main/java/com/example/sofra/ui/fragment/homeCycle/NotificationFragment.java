package com.example.sofra.ui.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.NotificationAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.model.notification.Notification;
import com.example.sofra.data.model.notification.NotificationData;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.activity.AuthActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class NotificationFragment extends BaseFragment {
    @BindView(R.id.fragment_notification_rv)
    RecyclerView fragmentNotificationRv;

    private ApiServices apiServices;
    private List<NotificationData> notificationData;
    private NotificationAdapter notificationAdapter;
    private String TAG = "notification";
    //    private String apiToken = "K1X6AzRlJFeVbGnHwGYsdCu0ETP1BqYC7DpMTZ3zLvKgU5feHMvsEEnKTpzh";
    private String apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
//        apiToken = LoadData(getActivity(), API_TOKEN);

        if (apiToken == (null)) {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }
        if (LoadData(getActivity(), USER_TYPE).equals("client")) {
            getClientNotification();
        } else if (LoadData(getActivity(), USER_TYPE).equals("restaurant")) {
            getRestaurantNotification();
        }
        initRecyc();
        return view;
    }

    private void initRecyc() {
        notificationData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationAdapter = new NotificationAdapter(getActivity(), notificationData);
        fragmentNotificationRv.setLayoutManager(linearLayoutManager);
        fragmentNotificationRv.setAdapter(notificationAdapter);
    }

    private void getClientNotification() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getNotification(apiToken).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        notificationData.addAll(response.body().getData().getData());
                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (
                        Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });
    }

    private void getRestaurantNotification() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getRestaurantNotification(apiToken).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        notificationData.addAll(response.body().getData().getData());
                        notificationAdapter.notifyDataSetChanged();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (
                        Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

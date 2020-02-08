package com.example.sofra.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.RateItemAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.Emoji;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.data.model.reviews.Reviews;
import com.example.sofra.helper.RecyclerItemClickListener;
import com.example.sofra.ui.fragment.authCycle.LoginFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.*;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.helper.HelperMethod.customToast;

public class RateDialog extends DialogFragment {
    @BindView(R.id.rate_dialog_rv_emojis)
    RecyclerView rvEmojis;
    @BindView(R.id.rate_dialog_ed_comment)
    EditText commentEd;

    private ApiServices apiServices;
    public Restaurant restaurant;
    private String apiToken;
    private String TAG = "Rate Dialog";
    private int rate = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_rate, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        apiServices = getClient().create(ApiServices.class);
        apiToken = LoadData(getActivity(), API_TOKEN);
        intiRecye(getActivity());
        return view;
    }

    private void intiRecye(Activity activity) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 5);
        List<Emoji> emojiList = new ArrayList<>();
        emojiList.add(new Emoji(R.drawable.ic_love));
        emojiList.add(new Emoji(R.drawable.ic_happy));
        emojiList.add(new Emoji(R.drawable.ic_smile));
        emojiList.add(new Emoji(R.drawable.ic_sad));
        emojiList.add(new Emoji(R.drawable.ic_angry));
        RateItemAdapter rateItemAdapter = new RateItemAdapter(activity, emojiList);
        rvEmojis.setLayoutManager(gridLayoutManager);
        rvEmojis.setAdapter(rateItemAdapter);
        rvEmojis.addOnItemTouchListener(
                new RecyclerItemClickListener(activity, (v, position) -> rate = position + 1)
        );
    }

    private void addRate(int rate, String comment, int restaurantId, Activity activity) {
        if (apiToken != (null)) {
            apiServices.addReviews(rate, comment, restaurantId, apiToken).enqueue(new Callback<Reviews>() {
                @Override
                public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            customToast(activity, response.body().getMsg(), false);

                        } else {
                            customToast(activity, response.body().getMsg(), true);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Reviews> call, Throwable t) {
                }
            });
        } else {
            Intent intent = new Intent(getActivity(), LoginFragment.class);
            startActivity(intent);
        }

    }

    @OnClick(R.id.add_review_dialog_btn_add)
    public void onViewClicked() {
        String comment = commentEd.getText().toString();
        if (rate != 0 && comment != null) {
            addRate(rate, comment, restaurant.getId(), getActivity());
        } else {
            customToast(getActivity(), "من فضلك ادخل التعليق والتقييم", true);
        }
    }
}

package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.ReviewsAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.data.model.reviews.Reviews;
import com.example.sofra.data.model.reviews.ReviewsData;
import com.example.sofra.ui.dialog.RateDialog;
import com.example.sofra.helper.OnEndLess;
import com.example.sofra.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.helper.HelperMethod.*;

public class RestaurantReviewsFragment extends BaseFragment {
    @BindView(R.id.fragment_reviews_rv_reviews)
    RecyclerView reviewsRv;

    private ApiServices apiServices;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter adapter;
    private List<ReviewsData> reviewsList;

    private OnEndLess onEndLess;
    private int MaxPage;
    private String TAG = "review -------";
    Restaurant restaurantDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_reviews, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        reviewsList = new ArrayList<>();
        adapter = new ReviewsAdapter(getActivity(), reviewsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        reviewsRv.setLayoutManager(linearLayoutManager);
        reviewsRv.setAdapter(adapter);
        getReviews(1);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= MaxPage) {
                    if (MaxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getReviews(current_page);
                        adapter.notifyDataSetChanged();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        reviewsRv.addOnScrollListener(onEndLess);
        return view;
    }

    private void getReviews(int page) {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getReviews(LoadData(getActivity(), REST_API_TOKEN), restaurantDate.getId(), page).enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        reviewsList.addAll(response.body().getData().getData());
                        MaxPage = response.body().getData().getLastPage();
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, response.message());
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.fragment_reviews_btn_add_comment)
    public void onViewClicked() {
        FragmentManager fragmentManager = getFragmentManager();
        RateDialog rateDialog = new RateDialog();
        rateDialog.show(fragmentManager, "rate dialog");
        rateDialog.restaurant = restaurantDate;
    }
}

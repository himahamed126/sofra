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
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.ui.fragment.homeCycle.CreateRestaurantOfferFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.replace;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ViewHolder> {
    private Context context;
    private List<OfferData> offersList;
    private ApiServices apiServices;
    private String TAG = "restaurant offer adapter";

    public RestaurantOffersAdapter(Context context, List<OfferData> offersList) {
        this.context = context;
        this.offersList = offersList;
        apiServices = getClient().create(ApiServices.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OfferData offer = offersList.get(position);
        holder.itemRestaurantOfferTvTitle.setText(offer.getName());
        onLoadImageFromUrl(holder.itemRestaurantOfferIvImage, offer.getPhotoUrl(), context);
    }

    private void setAction(ViewHolder holder, int position) {
        OfferData offer = offersList.get(position);
        holder.itemRestaurantOfferIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRestaurantOfferFragment createRestaurantOfferFragment = new CreateRestaurantOfferFragment();
                createRestaurantOfferFragment.offerData = offer;
                replace(createRestaurantOfferFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
            }
        });
        holder.itemRestaurantOfferIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOffer(offer.getId(), position);
            }
        });
    }

    private void deleteOffer(int offerId, int position) {
        showProgressDialog((AppCompatActivity) context, context.getString(R.string.please_wait));
        apiServices.deleteOffer(offerId, LoadData((AppCompatActivity) context, REST_API_TOKEN)).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast((AppCompatActivity) context, response.body().getMsg(), false);
                    offersList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_restaurant_offer_iv_image)
        CircleImageView itemRestaurantOfferIvImage;
        @BindView(R.id.item_restaurant_offer_tv_title)
        TextView itemRestaurantOfferTvTitle;
        @BindView(R.id.item_restaurant_offer_iv_delete)
        ImageView itemRestaurantOfferIvDelete;
        @BindView(R.id.item_restaurant_offer_iv_edit)
        ImageView itemRestaurantOfferIvEdit;
        @BindView(R.id.item_restaurant_offer_srl_swipe)
        SwipeRevealLayout itemRestaurantOfferSrlSwipe;
        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

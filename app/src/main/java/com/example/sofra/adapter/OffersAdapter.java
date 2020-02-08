package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.ui.fragment.homeCycle.ViewOfferDetailsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.replace;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {
    private Context context;
    private List<OfferData> offersList;

    public OffersAdapter(Context context, List<OfferData> offersList) {
        this.context = context;
        this.offersList = offersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        OfferData offer = offersList.get(position);
        holder.itemOfferTvTitle.setText(offer.getName());
        onLoadImageFromUrl(holder.itemOfferIvImage, offer.getPhotoUrl(), context);
    }

    private void setAction(ViewHolder holder, int position) {
        OfferData offer = offersList.get(position);
        holder.itemOfferBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewOfferDetailsFragment viewOfferDetailsFragment = new ViewOfferDetailsFragment();
                viewOfferDetailsFragment.offer = offer;
                replace(viewOfferDetailsFragment, ((AppCompatActivity) context).getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_offer_iv_image)
        CircleImageView itemOfferIvImage;
        @BindView(R.id.item_offer_tv_title)
        TextView itemOfferTvTitle;
        @BindView(R.id.item_offer_btn_details)
        TextView itemOfferBtnDetails;

        View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}

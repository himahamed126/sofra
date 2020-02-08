package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.offers.OfferData;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.helper.DateModel;
import com.example.sofra.ui.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.openGallery;
import static com.example.sofra.helper.HelperMethod.showCalender;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class CreateRestaurantOfferFragment extends BaseFragment {
    @BindView(R.id.fragment_create_restaurant_offer_tv_title)
    TextView fragmentCreateRestaurantOfferTvTitle;
    @BindView(R.id.fragment_create_restaurant_offer_iv_avatar)
    ImageView fragmentCreateRestaurantOfferIvAvatar;
    @BindView(R.id.fragment_create_restaurant_offer_ed_name)
    EditText fragmentCreateRestaurantOfferEdName;
    @BindView(R.id.fragment_create_restaurant_offer_ed_description)
    EditText fragmentCreateRestaurantOfferEdDescription;
    @BindView(R.id.fragment_create_restaurant_offer_tv_from)
    TextView fragmentCreateRestaurantOfferTvFrom;
    @BindView(R.id.fragment_create_restaurant_offer_tv_to)
    TextView fragmentCreateRestaurantOfferTvTo;
    @BindView(R.id.fragment_create_restaurant_offer_btn_add)
    TextView fragmentCreateRestaurantOfferBtnAdd;
    // no
    @BindView(R.id.fragment_create_restaurant_offer_ed_price)
    EditText fragmentCreateRestaurantOfferEdPrice;

    ApiServices apiServices;
    public OfferData offerData;
    private RequestBody description, price, starting_at, name, ending_at, api_token, offer_price, offerId;
    private MultipartBody.Part photo;
    private String photoUrl;
    private ArrayList<AlbumFile> albumFiles;
    private DateModel dateModel;
    private String TAG = "create restaurant offer";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_create_restaurant_offer, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        dateModel = new DateModel("01", "01", "2020", "01-01-2020");

        if (offerData != (null)) {
            setData();
        }
        return view;
    }

    private void setData() {
        fragmentCreateRestaurantOfferTvTitle.setText(getString(R.string.offer_image));
        fragmentCreateRestaurantOfferBtnAdd.setText(getString(R.string.edit));
        onLoadImageFromUrl(fragmentCreateRestaurantOfferIvAvatar, offerData.getPhotoUrl(), getActivity());
        fragmentCreateRestaurantOfferEdName.setText(offerData.getName());
        fragmentCreateRestaurantOfferEdDescription.setText(offerData.getDescription());
        fragmentCreateRestaurantOfferTvFrom.setText(offerData.getStartingAt());
        fragmentCreateRestaurantOfferTvTo.setText(offerData.getEndingAt());
        offerId = convertToRequestBody(String.valueOf(offerData.getId()));
    }

    private void convertData() {
        description = convertToRequestBody(fragmentCreateRestaurantOfferEdDescription.getText().toString());
        price = convertToRequestBody("0");
        offer_price = convertToRequestBody("0");
        starting_at = convertToRequestBody(fragmentCreateRestaurantOfferTvFrom.getText().toString());
        name = convertToRequestBody(fragmentCreateRestaurantOfferEdName.getText().toString());
        ending_at = convertToRequestBody(fragmentCreateRestaurantOfferTvTo.getText().toString());
        api_token = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
        photo = convertFileToMultipart(photoUrl, "photo");
    }

    private void addNewOffer() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.addNewOffer(description, price, starting_at, name, photo, ending_at, api_token, offer_price).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                } else {
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    private void updateOffer() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.updateOffer(description, price, starting_at, name, photo, ending_at, offerId, api_token).enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                } else {
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.fragment_create_restaurant_offer_iv_avatar, R.id.fragment_create_restaurant_offer_btn_add,
            R.id.fragment_create_restaurant_offer_btn_from, R.id.fragment_create_restaurant_offer_btn_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_create_restaurant_offer_iv_avatar:
                openGallery(getActivity(), albumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        photoUrl = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentCreateRestaurantOfferIvAvatar, photoUrl, getActivity());
                    }
                }, 1);
                break;
            case R.id.fragment_create_restaurant_offer_btn_add:
                if (offerData != (null)) {
                    updateOffer();
                } else {
                    addNewOffer();
                }
                break;
            case R.id.fragment_create_restaurant_offer_btn_from:
                showCalender(getActivity(), getString(R.string.start_offer), fragmentCreateRestaurantOfferTvFrom, dateModel);
                break;
            case R.id.fragment_create_restaurant_offer_btn_to:
                showCalender(getActivity(), getString(R.string.end_offer), fragmentCreateRestaurantOfferTvTo, dateModel);
                break;
        }
    }
}

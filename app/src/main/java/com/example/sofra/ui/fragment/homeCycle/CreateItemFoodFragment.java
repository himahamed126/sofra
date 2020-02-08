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
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.data.model.publicData.GeneralResponse;
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
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class CreateItemFoodFragment extends BaseFragment {
    @BindView(R.id.fragment_create_item_food_iv_avatar)
    ImageView fragmentCreateItemFoodIvAvatar;
    @BindView(R.id.fragment_create_item_food_ed_name)
    EditText fragmentCreateItemFoodEdName;
    @BindView(R.id.fragment_create_item_food_ed_description)
    EditText fragmentCreateItemFoodEdDescription;
    @BindView(R.id.fragment_create_item_food_ed_price)
    EditText fragmentCreateItemFoodEdPrice;
    @BindView(R.id.fragment_create_item_food_ed_price_in_offer)
    EditText fragmentCreateItemFoodEdPriceInOffer;
    @BindView(R.id.fragment_create_item_food_tv_title)
    TextView fragmentCreateItemFoodTvTitle;
    @BindView(R.id.fragment_create_item_food_btn_add)
    TextView fragmentCreateItemFoodBtnAdd;

    private ApiServices apiServices;
    private String TAG = "create item food";

    private ArrayList<AlbumFile> albumFile;

    CategoryData categoryData;
    public Item item;
    private RequestBody description, price, preparing_time, name, api_token, offer_price, category_id, item_id;
    private MultipartBody.Part photo;
    private String photoUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_create_item_food, container, false);

        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        if (item != (null)) {
            setData();
            category_id = convertToRequestBody(String.valueOf(item.getCategoryId()));
            item_id = convertToRequestBody(String.valueOf(item.getId()));
        } else {
            category_id = convertToRequestBody(String.valueOf(categoryData.getId()));
        }
        return view;
    }

    private void setData() {
        fragmentCreateItemFoodEdName.setText(item.getName());
        fragmentCreateItemFoodEdDescription.setText(item.getDescription());
        fragmentCreateItemFoodEdPrice.setText(item.getPrice());
        fragmentCreateItemFoodEdPriceInOffer.setText(item.getOfferPrice());
        onLoadImageFromUrl(fragmentCreateItemFoodIvAvatar, item.getPhotoUrl(), getActivity());
        fragmentCreateItemFoodTvTitle.setText(getString(R.string.product_image));
        fragmentCreateItemFoodBtnAdd.setText(getString(R.string.edit));
    }

    private void convertData() {
        description = convertToRequestBody(fragmentCreateItemFoodEdDescription.getText().toString());
        price = convertToRequestBody(fragmentCreateItemFoodEdPrice.getText().toString());
        preparing_time = convertToRequestBody(fragmentCreateItemFoodEdDescription.getText().toString());
        name = convertToRequestBody(fragmentCreateItemFoodEdName.getText().toString());
        photo = convertFileToMultipart(photoUrl, "photo");
        api_token = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
        offer_price = convertToRequestBody(fragmentCreateItemFoodEdPriceInOffer.getText().toString());
    }

    private void addFoodItem() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.addItem(description, price, preparing_time, name, photo, api_token, offer_price, category_id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                } else {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void updateFoodItem() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.updateItem(description, price, category_id, name, photo, item_id, api_token, offer_price).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                        CreateItemFoodFragment.super.onBack();
                        Log.d(TAG, response.body().getMsg());
                    } else {
                        Log.d(TAG, response.body().getMsg());
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.fragment_create_item_food_iv_avatar, R.id.fragment_create_item_food_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_create_item_food_iv_avatar:
                openGallery(getActivity(), albumFile, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        photoUrl = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentCreateItemFoodIvAvatar, photoUrl, getActivity());
                    }
                }, 1);
                break;
            case R.id.fragment_create_item_food_btn_add:
                if (item == (null)) {
                    addFoodItem();
                } else {
                    updateFoodItem();
                }
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.publicData.RegionData;
import com.example.sofra.ui.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.REST_AVAILABILITY;
import static com.example.sofra.data.local.SofraConstans.REST_CITY_ID;
import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYCOAST;
import static com.example.sofra.data.local.SofraConstans.REST_DELIVERYTIME;
import static com.example.sofra.data.local.SofraConstans.REST_EMAIL;
import static com.example.sofra.data.local.SofraConstans.REST_MINIUMCHARGE;
import static com.example.sofra.data.local.SofraConstans.REST_NAME;
import static com.example.sofra.data.local.SofraConstans.REST_PHONE;
import static com.example.sofra.data.local.SofraConstans.REST_IMAGE;
import static com.example.sofra.data.local.SofraConstans.REST_REGION_ID;
import static com.example.sofra.data.local.SofraConstans.REST_WHATSAPP;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.openGallery;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class EditRestaurantProfileFragment extends BaseFragment {
    @BindView(R.id.fragment_edit_restaurant_profile_iv_avatar)
    CircleImageView fragmentEditRestaurantProfileIvAvatar;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_name)
    EditText fragmentEditRestaurantProfileEdName;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_email)
    EditText fragmentEditRestaurantProfileEdEmail;
    @BindView(R.id.fragment_edit_restaurant_profile_sp_city)
    AppCompatSpinner fragmentEditRestaurantProfileSpCity;
    @BindView(R.id.fragment_edit_restaurant_profile_sp_region)
    AppCompatSpinner fragmentEditRestaurantProfileSpRegion;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_minim_charge)
    EditText fragmentEditRestaurantProfileEdMinimCharge;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_dlivery_coast)
    EditText fragmentEditRestaurantProfileEdDliveryCoast;
    @BindView(R.id.fragment_edit_restaurant_profile_sw_status)
    Switch fragmentEditRestaurantProfileSwStatus;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_phone)
    EditText fragmentEditRestaurantProfileEdPhone;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_whats_up)
    EditText fragmentEditRestaurantProfileEdWhatsUp;
    @BindView(R.id.fragment_edit_restaurant_profile_ed_dlivery_time)
    EditText fragmentEditRestaurantProfileEdDliveryTime;

    private ApiServices apiServices;
    private ArrayList<AlbumFile> albumFiles;
    private String imageUrl;

    private RequestBody email, name, phone, regionId, deliveryCost, minimCharge, availability, apiToken, deliveryTime, whatsApp;
    private MultipartBody.Part profileImage;

    private GeneralResponseAdapter generalResponseAdapter;
    private List<RegionData> city;
    private List<RegionData> region;
    private String TAG = "edit profile restaurant";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_edit_restaurant_profile, container, false);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        addCity();
        generalResponseAdapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.select_city));
        fragmentEditRestaurantProfileSpCity.setAdapter(generalResponseAdapter);

        setData();
        return view;
    }

    private void addCity() {
        city = new ArrayList<>();
        apiServices.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    city.addAll(response.body().getData().getData());

                    fragmentEditRestaurantProfileSpCity.setSelection(Integer.parseInt(LoadData(getActivity(), REST_CITY_ID)));
                    addRegion(fragmentEditRestaurantProfileSpCity.getSelectedItemPosition());

                    generalResponseAdapter = new GeneralResponseAdapter(getActivity(), region, getString(R.string.select_region));
                    fragmentEditRestaurantProfileSpRegion.setAdapter(generalResponseAdapter);
                    generalResponseAdapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void addRegion(int cityId) {
        region = new ArrayList<>();
        apiServices.getRegions(cityId).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    region.addAll(response.body().getData().getData());
                    fragmentEditRestaurantProfileSpRegion.setSelection(Integer.parseInt(LoadData(getActivity(), REST_REGION_ID)));
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void setData() {
        fragmentEditRestaurantProfileEdName.setText(LoadData(getActivity(), REST_NAME));
        fragmentEditRestaurantProfileEdEmail.setText(LoadData(getActivity(), REST_EMAIL));
        fragmentEditRestaurantProfileEdMinimCharge.setText(LoadData(getActivity(), REST_MINIUMCHARGE));
        fragmentEditRestaurantProfileEdDliveryCoast.setText(LoadData(getActivity(), REST_DELIVERYCOAST));
        fragmentEditRestaurantProfileEdPhone.setText(LoadData(getActivity(), REST_PHONE));
        fragmentEditRestaurantProfileEdWhatsUp.setText(LoadData(getActivity(), REST_WHATSAPP));
        fragmentEditRestaurantProfileEdDliveryTime.setText(LoadData(getActivity(), REST_DELIVERYTIME));
        onLoadImageFromUrl(fragmentEditRestaurantProfileIvAvatar, LoadData(getActivity(), REST_IMAGE), getActivity());
        if (LoadData(getActivity(), REST_AVAILABILITY).equals("open")) {
            fragmentEditRestaurantProfileSwStatus.setChecked(true);
        } else {
            fragmentEditRestaurantProfileSwStatus.setChecked(false);
        }
    }

    private void convertData() {
        email = convertToRequestBody(fragmentEditRestaurantProfileEdEmail.getText().toString());
        name = convertToRequestBody(fragmentEditRestaurantProfileEdName.getText().toString());
        phone = convertToRequestBody(fragmentEditRestaurantProfileEdPhone.getText().toString());
        deliveryCost = convertToRequestBody(fragmentEditRestaurantProfileEdDliveryCoast.getText().toString());
        minimCharge = convertToRequestBody(fragmentEditRestaurantProfileEdMinimCharge.getText().toString());
        apiToken = convertToRequestBody(LoadData(getActivity(), REST_API_TOKEN));
        deliveryTime = convertToRequestBody(fragmentEditRestaurantProfileEdEmail.getText().toString());
        whatsApp = convertToRequestBody(fragmentEditRestaurantProfileEdWhatsUp.getText().toString());
        deliveryTime = convertToRequestBody(fragmentEditRestaurantProfileEdDliveryTime.getText().toString());
        profileImage = convertFileToMultipart(imageUrl, "profile_image");
        fragmentEditRestaurantProfileSwStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    availability = convertToRequestBody("open");
                } else {
                    availability = convertToRequestBody("closed");
                }
            }
        });
        regionId = convertToRequestBody(String.valueOf(fragmentEditRestaurantProfileSpRegion.getSelectedItemPosition()));
    }

    private void editRestaurant() {
        convertData();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.editProfileRestaurant(email, name, phone, regionId, deliveryCost, minimCharge, availability, profileImage,
                apiToken, deliveryTime, whatsApp).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                    Log.i(TAG, response.body().getMsg());
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                    Log.i(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.fragment_edit_restaurant_profile_iv_avatar, R.id.fragment_edit_restaurant_profile_btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_edit_restaurant_profile_iv_avatar:
                openGallery(getActivity(), albumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imageUrl = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentEditRestaurantProfileIvAvatar, imageUrl, getActivity());
                    }
                }, 1);
                break;
            case R.id.fragment_edit_restaurant_profile_btn_edit:
                editRestaurant();
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

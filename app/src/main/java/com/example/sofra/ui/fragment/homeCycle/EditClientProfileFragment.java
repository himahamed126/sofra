package com.example.sofra.ui.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.publicData.RegionData;
import com.example.sofra.ui.BaseFragment;
import com.example.sofra.ui.activity.AuthActivity;
import com.example.sofra.ui.fragment.authCycle.LoginFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

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
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.data.local.SofraConstans.API_TOKEN;
import static com.example.sofra.data.local.SofraConstans.CITY_ID;
import static com.example.sofra.data.local.SofraConstans.EMAIL;
import static com.example.sofra.data.local.SofraConstans.NAME;
import static com.example.sofra.data.local.SofraConstans.PASSWORD;
import static com.example.sofra.data.local.SofraConstans.PHONE;
import static com.example.sofra.data.local.SofraConstans.PROFILE_IMAGE;
import static com.example.sofra.data.local.SofraConstans.REGION_ID;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.openGallery;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class EditClientProfileFragment extends BaseFragment {
    @BindView(R.id.fragment_edit_client_profile_iv_avatar)
    CircleImageView fragmentEditClientProfileIvAvatar;
    @BindView(R.id.fragment_edit_client_profile_ed_name)
    EditText fragmentEditClientProfileEdName;
    @BindView(R.id.fragment_edit_client_profile_ed_email)
    EditText fragmentEditClientProfileEdEmail;
    @BindView(R.id.fragment_edit_client_profile_ed_phone)
    EditText fragmentEditClientProfileEdPhone;
    @BindView(R.id.fragment_edit_client_profile_sp_city)
    AppCompatSpinner fragmentEditClientProfileSpCity;
    @BindView(R.id.fragment_edit_client_profile_sp_region)
    AppCompatSpinner fragmentEditClientProfileSpRegion;

    private ApiServices apiServices;
    private GeneralResponseAdapter generalAdapter;
    private ArrayList<RegionData> city, region;
    private ArrayList<AlbumFile> albumFiles;

    private RequestBody apiToken, name, email, phone, password, passwordConfirmation, regionId;
    private MultipartBody.Part profileImage;
    private String avatar;
    private String TAG = "edit client";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_edit_client_profile, container, false);

        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        if (LoadData(getActivity(), API_TOKEN) != (null)) {
            addCity();
            generalAdapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.select_city));
            fragmentEditClientProfileSpCity.setAdapter(generalAdapter);
            generalAdapter.notifyDataSetChanged();
            setData();
        } else {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }

        return view;
    }

    private void addCity() {
        city = new ArrayList<>();
        apiServices.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    city.addAll(response.body().getData().getData());
                    if (LoadData(getActivity(), CITY_ID) != (null)) {
                        fragmentEditClientProfileSpCity.setSelection(Integer.parseInt(LoadData(getActivity(), CITY_ID)));
                    }
                    addRegion(fragmentEditClientProfileSpCity.getSelectedItemPosition());
                    generalAdapter = new GeneralResponseAdapter(getActivity(), region, getString(R.string.select_region));
                    fragmentEditClientProfileSpRegion.setAdapter(generalAdapter);
                    generalAdapter.notifyDataSetChanged();
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
                    if (LoadData(getActivity(), REGION_ID) != (null)) {
                        fragmentEditClientProfileSpRegion.setSelection(Integer.parseInt(LoadData(getActivity(), REGION_ID)));
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void setData() {
        onLoadImageFromUrl(fragmentEditClientProfileIvAvatar, LoadData(getActivity(), PROFILE_IMAGE), getActivity());
        fragmentEditClientProfileEdName.setText(LoadData(getActivity(), NAME));
        fragmentEditClientProfileEdEmail.setText(LoadData(getActivity(), EMAIL));
        fragmentEditClientProfileEdPhone.setText(LoadData(getActivity(), PHONE));
    }

    private void convertData() {
        apiToken = convertToRequestBody(LoadData(getActivity(), API_TOKEN));
        name = convertToRequestBody(fragmentEditClientProfileEdName.getText().toString());
        email = convertToRequestBody(fragmentEditClientProfileEdEmail.getText().toString());
        phone = convertToRequestBody(fragmentEditClientProfileEdPhone.getText().toString());
        regionId = convertToRequestBody(String.valueOf(fragmentEditClientProfileSpRegion.getSelectedItemPosition()));
        password = convertToRequestBody(LoadData(getActivity(), PASSWORD));
        passwordConfirmation = convertToRequestBody(LoadData(getActivity(), PASSWORD));
        profileImage = convertFileToMultipart(avatar, "profile_image");
    }

    private void editProfile() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        convertData();
        apiServices.editProfileClient(apiToken, name, phone, email, password, passwordConfirmation, regionId, profileImage).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                        SaveData(getActivity(), NAME, fragmentEditClientProfileEdName.getText().toString());
                        SaveData(getActivity(), EMAIL, fragmentEditClientProfileEdEmail.getText().toString());
                        SaveData(getActivity(), PHONE, fragmentEditClientProfileEdPhone.getText().toString());
                        SaveData(getActivity(), CITY_ID, fragmentEditClientProfileSpCity.getSelectedItemPosition());
                        SaveData(getActivity(), REGION_ID, fragmentEditClientProfileSpRegion.getSelectedItemPosition());
                        SaveData(getActivity(), PROFILE_IMAGE, avatar);
                    } else {
                        customToast(getActivity(), response.body().getMsg(), false);
                    }
                } catch (Exception e) {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.fragment_edit_client_profile_btn_edit, R.id.fragment_edit_client_profile_iv_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_edit_client_profile_btn_edit:
                editProfile();
                break;
            case R.id.fragment_edit_client_profile_iv_avatar:
                openGallery(getActivity(), albumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        avatar = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentEditClientProfileIvAvatar, avatar, getActivity());
                    }
                }, 1);
                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

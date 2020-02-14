package com.example.sofra.ui.fragment.authCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.publicData.RegionData;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.*;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class RegisterClientFragment extends BaseFragment {
    @BindView(R.id.fragment_register_client_iv_add_image)
    ImageView fragmentRegisterClientIvAddImage;
    @BindView(R.id.fragment_register_client_ed_client_name)
    EditText fragmentRegisterClientEdClientName;
    @BindView(R.id.fragment_register_client_ed_email)
    EditText fragmentRegisterClientEdEmail;
    @BindView(R.id.fragment_register_client_ed_phone)
    EditText fragmentRegisterClientEdPhone;
    @BindView(R.id.fragment_register_client_sp_city)
    AppCompatSpinner fragmentRegisterClientSpCity;
    @BindView(R.id.fragment_register_client_sp_region)
    AppCompatSpinner fragmentRegisterClientSpRegion;
    @BindView(R.id.fragment_register_client_ed_password)
    EditText fragmentRegisterClientEdPassword;
    @BindView(R.id.fragment_register_client_ed_confirm_password)
    EditText fragmentRegisterClientEdConfirmPassword;

    private ApiServices apiServices;
    private Unbinder unbinder;

    private GeneralResponseAdapter generalAdapter;
    private ArrayList<RegionData> city, region;

    private String imagePath;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    private String TAG = "register client";
    private RequestBody name, email, phone, password, confirmPassword, regionId;
    private MultipartBody.Part photo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_auth_register_client, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);

        getCities();
        generalAdapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.city));
        fragmentRegisterClientSpCity.setAdapter(generalAdapter);
        generalAdapter.notifyDataSetChanged();

        fragmentRegisterClientSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getRegion(position);
                generalAdapter = new GeneralResponseAdapter(getActivity(), region, getString(R.string.region));
                fragmentRegisterClientSpRegion.setAdapter(generalAdapter);
                generalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void getCities() {
        city = new ArrayList<>();
        apiServices.getCities().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        city.addAll(response.body().getData().getData());
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void getRegion(int cityId) {
        region = new ArrayList<>();
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.getRegions(cityId).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        region.addAll(response.body().getData().getData());
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    private void convertData() {
        name = convertToRequestBody(fragmentRegisterClientEdClientName.getText().toString());
        email = convertToRequestBody(fragmentRegisterClientEdEmail.getText().toString());
        phone = convertToRequestBody(fragmentRegisterClientEdPhone.getText().toString());
        password = convertToRequestBody(fragmentRegisterClientEdPassword.getText().toString());
        confirmPassword = convertToRequestBody(fragmentRegisterClientEdConfirmPassword.getText().toString());
        regionId = convertToRequestBody(String.valueOf(fragmentRegisterClientSpRegion.getSelectedItemPosition()));
        photo = convertFileToMultipart(imagePath, "profile_image");
    }

    private void register() {
        convertData();
        apiServices.getClientRegister(name, email, password, confirmPassword, phone, regionId, photo).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                    } else {
                        Log.i(TAG, response.body().getMsg());
                    }
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @OnClick({R.id.fragment_register_client_btn_register, R.id.fragment_register_client_iv_add_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_register_client_btn_register:
                register();
                break;
            case R.id.fragment_register_client_iv_add_image:
                openGallery(getActivity(), mAlbumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagePath = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentRegisterClientIvAddImage, imagePath, getActivity());
                    }
                }, 1);
                break;
        }
    }

    @Override
    public void onBack() {
        LoginFragment loginFragment = new LoginFragment();
        replace(loginFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }
}

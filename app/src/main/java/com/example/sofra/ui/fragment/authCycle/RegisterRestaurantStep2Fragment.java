package com.example.sofra.ui.fragment.authCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.helper.MediaLoader;
import com.example.sofra.ui.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
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
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.openGallery;

public class RegisterRestaurantStep2Fragment extends BaseFragment {
    @BindView(R.id.fragment_register_s2_ed_phone)
    EditText fragmentRegisterS2EdPhone;
    @BindView(R.id.fragment_register_s2_ed_whats_up)
    EditText fragmentRegisterS2EdWhatsUp;
    @BindView(R.id.fragment_register_s2_iv_store_image)
    ImageView fragmentRegisterS2IvStoreImage;

    private Unbinder unbinder;
    private ApiServices apiServices;

    private ArrayList<AlbumFile> mAlbumFiles;
    private String imagePath;
    private String name, email, deliveryTime, password, confirmPassword, minimumOrder, deliveryCharge;
    private int regionId;
    private String TAG = "register restaurant";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_register_restaurant_step2, container, false);
        setUpActivity();
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        return view;
    }

    private void register() {
        String phone = fragmentRegisterS2EdPhone.getText().toString().trim();
        String whatsapp = fragmentRegisterS2EdWhatsUp.getText().toString().trim();

        if (getArguments() != null) {
            name = getArguments().getString("Restaurant_Name");
            email = getArguments().getString("Restaurant_Email");
            deliveryTime = getArguments().getString("Restaurant_DeliveryTime");
            password = getArguments().getString("Restaurant_Password");
            confirmPassword = getArguments().getString("Restaurant_ConfirmationPassword");
            minimumOrder = getArguments().getString("Restaurant_MinimumOrder");
            deliveryCharge = getArguments().getString("Restaurant_DeliveryCharge");
            regionId = getArguments().getInt("Restaurant_RegionId");
        }
        RequestBody nameBody = convertToRequestBody(name);
        RequestBody emailBody = convertToRequestBody(email);
        RequestBody deliveryTimeBody = convertToRequestBody(deliveryTime);
        RequestBody passwordBody = convertToRequestBody(password);
        RequestBody confirmPasswordBody = convertToRequestBody(confirmPassword);
        RequestBody minimumOrderBody = convertToRequestBody(minimumOrder);
        RequestBody deliveryChargeBody = convertToRequestBody(deliveryCharge);
        RequestBody phoneBody = convertToRequestBody(phone);
        RequestBody whatsappBody = convertToRequestBody(whatsapp);
        RequestBody regionIdBody = convertToRequestBody(String.valueOf(regionId));
        MultipartBody.Part imageMultipartBody = convertFileToMultipart(imagePath, "photo");

        apiServices.getRestaurantRegister(nameBody, emailBody, passwordBody, confirmPasswordBody, phoneBody,
                whatsappBody, regionIdBody, deliveryChargeBody, minimumOrderBody, imageMultipartBody, deliveryTimeBody).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG, response.body().getMsg());
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
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


    @OnClick({R.id.fragment_register_s2_iv_store_image, R.id.fragment_forgotten_password_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_register_s2_iv_store_image:
                openGallery(getActivity(), mAlbumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        imagePath = result.get(0).getPath();
                        onLoadImageFromUrl(fragmentRegisterS2IvStoreImage, imagePath, getActivity());
                    }
                }, 1);
                break;
            case R.id.fragment_forgotten_password_btn_send:
                register();
                break;
        }
    }

    @Override
    public void onBack() {
        RegisterRestaurantStep1Fragment registerRestaurantStep1Fragment = new RegisterRestaurantStep1Fragment();
        HelperMethod.replace(registerRestaurantStep1Fragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }
}

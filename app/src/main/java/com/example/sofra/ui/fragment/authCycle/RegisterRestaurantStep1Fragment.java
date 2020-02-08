package com.example.sofra.ui.fragment.authCycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.sofra.R;
import com.example.sofra.adapter.GeneralResponseAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.publicData.RegionData;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.replace;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class RegisterRestaurantStep1Fragment extends BaseFragment {
    @BindView(R.id.fragment_register_ed_restaurant_name)
    EditText fragmentRegisterEdRestaurantName;
    @BindView(R.id.fragment_register_ed_email)
    EditText fragmentRegisterEdEmail;
    @BindView(R.id.fragment_register_ed_delivery_time)
    EditText fragmentRegisterEdDeliveryTime;
    @BindView(R.id.fragment_register_restaurant_sp_city)
    AppCompatSpinner fragmentRegisterRestaurantSpCity;
    @BindView(R.id.fragment_register_restaurant_sp_region)
    AppCompatSpinner fragmentRegisterRestaurantSpRegion;
    @BindView(R.id.fragment_register_ed_password)
    EditText fragmentRegisterEdPassword;
    @BindView(R.id.fragment_register_confirm_password)
    EditText fragmentRegisterConfirmPassword;
    @BindView(R.id.fragment_register_ed_minimum_order)
    EditText fragmentRegisterEdMinimumOrder;
    @BindView(R.id.fragment_register_ed_delivery_charge)
    EditText fragmentRegisterEdDeliveryCharge;

    private Unbinder unbinder;
    private ApiServices apiServices;
    private GeneralResponseAdapter generalAdapter;
    private ArrayList<RegionData> city, region;

    private Bundle bundle;
    private int RegionId;
    private String TAG = "register restaurant";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_auth_register_restaurant_step1, container, false);
        setUpActivity();
        unbinder = ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        getCities();

        generalAdapter = new GeneralResponseAdapter(getActivity(), city, getString(R.string.city));
        fragmentRegisterRestaurantSpCity.setAdapter(generalAdapter);
        generalAdapter.notifyDataSetChanged();

        fragmentRegisterRestaurantSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getRegion(position);
                generalAdapter = new GeneralResponseAdapter(getActivity(), region, getString(R.string.region));
                fragmentRegisterRestaurantSpRegion.setAdapter(generalAdapter);
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

    private void checkAndSendData() {
        String name = fragmentRegisterEdRestaurantName.getText().toString().trim();
        String email = fragmentRegisterEdEmail.getText().toString().trim();
        String deliveryTime = fragmentRegisterEdDeliveryTime.getText().toString().trim();
        String password = fragmentRegisterEdPassword.getText().toString().trim();
        String confirmationPassword = fragmentRegisterConfirmPassword.getText().toString().trim();
        String minimumOrder = fragmentRegisterEdMinimumOrder.getText().toString().trim();
        String deliveryCharge = fragmentRegisterEdDeliveryCharge.getText().toString().trim();


        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "please insert name", Toast.LENGTH_LONG).show();
        }
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "please insert email", Toast.LENGTH_LONG).show();
        }
        if (deliveryTime.isEmpty()) {
            Toast.makeText(getActivity(), "please insert deliveryTime", Toast.LENGTH_LONG).show();
        }
        if (password.isEmpty()) {
            Toast.makeText(getActivity(), "please insert password", Toast.LENGTH_LONG).show();
        }
        if (confirmationPassword.isEmpty()) {
            Toast.makeText(getActivity(), "please insert confirmationPassword", Toast.LENGTH_LONG).show();
        }
        if (minimumOrder.isEmpty()) {
            Toast.makeText(getActivity(), "please insert minimumOrder", Toast.LENGTH_LONG).show();
        }
        if (deliveryCharge.isEmpty()) {
            Toast.makeText(getActivity(), "please insert deliveryCharge", Toast.LENGTH_LONG).show();
        }

        RegionId = fragmentRegisterRestaurantSpRegion.getSelectedItemPosition();
        if (RegionId == 0) {
            Toast.makeText(getActivity(), "please insert RegionId", Toast.LENGTH_LONG).show();
        }

        bundle = new Bundle();
        bundle.putString("Restaurant_Name", name);
        bundle.putString("Restaurant_Email", email);
        bundle.putString("Restaurant_DeliveryTime", deliveryTime);
        bundle.putString("Restaurant_Password", password);
        bundle.putString("Restaurant_ConfirmationPassword", confirmationPassword);
        bundle.putString("Restaurant_MinimumOrder", minimumOrder);
        bundle.putString("Restaurant_DeliveryCharge", deliveryCharge);
        bundle.putInt("Restaurant_RegionId", RegionId);

        RegisterRestaurantStep2Fragment registerRestaurantStep2Fragment = new RegisterRestaurantStep2Fragment();
        registerRestaurantStep2Fragment.setArguments(bundle);
        replace(registerRestaurantStep2Fragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }

    @OnClick(R.id.fragment_register_s1_btn_register)
    void onViewClicked() {
        checkAndSendData();
    }

    @Override
    public void onBack() {
        LoginFragment loginFragment = new LoginFragment();
        replace(loginFragment, getActivity().getSupportFragmentManager(), R.id.activity_auth_fl_fragment, null, null);
    }
}

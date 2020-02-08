package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.model.contactUs.ContactUs;
import com.example.sofra.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.example.sofra.helper.HelperMethod.showProgressDialog;

public class ContactUsFragment extends BaseFragment {
    @BindView(R.id.fragment_contact_us_ed_name)
    EditText fragmentContactUsEdName;
    @BindView(R.id.fragment_contact_us_ed_email)
    EditText fragmentContactUsEdEmail;
    @BindView(R.id.fragment_contact_us_ed_phone)
    EditText fragmentContactUsEdPhone;
    @BindView(R.id.fragment_contact_us_ed_message)
    EditText fragmentContactUsEdMessage;
    @BindView(R.id.fragment_contact_us_rg_message_type)
    RadioGroup radioGroup;

    ApiServices apiServices;
    private String type;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this, view);

        apiServices = getClient().create(ApiServices.class);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_contact_us_rb_complain:
                        type = "complaint";
                        break;
                    case R.id.fragment_contact_us_rb_suggestion:
                        type = "suggestion";
                        break;
                    case R.id.fragment_contact_us_rb_enquiry:
                        type = "inquiry";
                        break;
                }
            }
        });

        return view;
    }

    private void sendMessage() {
        showProgressDialog(getActivity(), getString(R.string.please_wait));
        apiServices.sendMessage(fragmentContactUsEdName.getText().toString(),
                fragmentContactUsEdEmail.getText().toString(),
                fragmentContactUsEdPhone.getText().toString(), type,
                fragmentContactUsEdMessage.getText().toString()).enqueue(new Callback<ContactUs>() {
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(getActivity(), response.body().getMsg(), false);
                } else {
                    customToast(getActivity(), response.body().getMsg(), false);
                }
            }

            @Override
            public void onFailure(Call<ContactUs> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.fragment_contact_us_btn_send)
    public void onViewClicked() {
        sendMessage();
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

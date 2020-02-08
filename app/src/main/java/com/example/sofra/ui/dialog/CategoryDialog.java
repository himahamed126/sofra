package com.example.sofra.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sofra.R;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.SharedPreferencesManger;
import com.example.sofra.data.model.publicData.CategoryData;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.ui.fragment.homeCycle.RestaurantCategoriesFragment;
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
import static com.example.sofra.data.local.SofraConstans.REST_API_TOKEN;
import static com.example.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.example.sofra.helper.HelperMethod.convertToRequestBody;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.example.sofra.helper.HelperMethod.openGallery;

public class CategoryDialog extends DialogFragment {
    @BindView(R.id.dialog_restaurant_category_tv_title)
    TextView dialogRestaurantCategoryTvTitle;
    @BindView(R.id.dialog_restaurant_category_iv_image)
    CircleImageView dialogRestaurantCategoryIvImage;
    @BindView(R.id.dialog_restaurant_category_ed_category_name)
    EditText dialogRestaurantCategoryEdCategoryName;
    @BindView(R.id.dialog_restaurant_category_btn)
    TextView dialogRestaurantCategoryBtn;
    private ApiServices apiServices;
    private ArrayList<AlbumFile> mAlbumFiles;
    private String imagePath;
    private String TAG = "add category";
    private RestaurantCategoriesFragment restaurantCategoriesFragment;

    private RequestBody apiTokenBody, categoryNameBody;
    private MultipartBody.Part imageBody;

    public CategoryData categoryData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialoge_add_restaurant_categories, container);
        ButterKnife.bind(this, view);
        apiServices = getClient().create(ApiServices.class);
        restaurantCategoriesFragment = new RestaurantCategoriesFragment();

        apiTokenBody = convertToRequestBody(SharedPreferencesManger.LoadData(getActivity(), REST_API_TOKEN));

        if (categoryData != (null)) {
            dialogRestaurantCategoryEdCategoryName.setText(categoryData.getName());
            onLoadImageFromUrl(dialogRestaurantCategoryIvImage, categoryData.getPhotoUrl(), getActivity());
            dialogRestaurantCategoryTvTitle.setText(getString(R.string.category_edit));
            dialogRestaurantCategoryBtn.setText(getString(R.string.edit));
        }
        return view;
    }

    private void addRestaurantCategory() {
        apiServices.addNewCategory(categoryNameBody, imageBody, apiTokenBody).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        customToast(getActivity(), response.body().getMsg(), false);
                        getDialog().cancel();
                    } else {
                        Log.i(TAG, response.body().getMsg());
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

    private void updateRestaurantCategory(int categoryId) {
        apiServices.updateCategory(categoryNameBody, imageBody, apiTokenBody, convertToRequestBody(String.valueOf(categoryId)))
                .enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                customToast(getActivity(), response.body().getMsg(), false);
                                getDialog().cancel();
                            } else {
                                Log.i(TAG, response.body().getMsg());
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

    @OnClick({R.id.dialog_restaurant_category_iv_image, R.id.dialog_restaurant_category_btn})
    public void onViewClicked(View view) {
        categoryNameBody = convertToRequestBody(dialogRestaurantCategoryEdCategoryName.getText().toString().trim());
        imageBody = convertFileToMultipart(imagePath, "photo");
        switch (view.getId()) {
            case R.id.dialog_restaurant_category_iv_image:
                openGallery(getActivity(), mAlbumFiles, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagePath = result.get(0).getPath();
                        onLoadImageFromUrl(dialogRestaurantCategoryIvImage, imagePath, getActivity());
                    }
                }, 1);
                break;
            case R.id.dialog_restaurant_category_btn:
                if (categoryData != (null)) {
                    updateRestaurantCategory(categoryData.getId());
                } else {
                    addRestaurantCategory();
                }
                break;
        }
    }
}

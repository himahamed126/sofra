package com.example.sofra.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.adapter.OrderAdapter;
import com.example.sofra.data.api.ApiServices;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HelperMethod {
    private static ProgressDialog checkDialog;
    private static RecyclerView recyclerView = null;
    static Activity activity;

    public static void replace(Fragment fragment, FragmentManager supportFragmentManager, int id, TextView Tool_Bar_Title, String title) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        if (Tool_Bar_Title != null) {
            Tool_Bar_Title.setText(title);
        }
    }

    public static void showCalender(Context context, String title, final TextView text_view_data, final DateModel data1) {

        DatePickerDialog mDatePicker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                String data = selectedYear + "-" + String.format(new Locale("en"), mFormat.format(Double.valueOf((selectedMonth + 1)))) + "-"
                        + mFormat.format(Double.valueOf(selectedDay));
                data1.setDate_txt(data);
                data1.setDay(mFormat.format(Double.valueOf(selectedDay)));
                data1.setMonth(mFormat.format(Double.valueOf(selectedMonth + 1)));
                data1.setYear(String.valueOf(selectedYear));
                if (text_view_data != null) {
                    text_view_data.setText(data);
                }
            }
        }, Integer.parseInt(data1.getYear()), Integer.parseInt(data1.getMonth()) - 1, Integer.parseInt(data1.getDay()));
        mDatePicker.setTitle(title);
        mDatePicker.show();
    }

    public static Date convertDateString(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date parse = format.parse(date);

            return parse;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DateModel convertStringToDateTxtModel(String date) {
        try {
            Date date1 = convertDateString(date);
            String day = (String) DateFormat.format("dd", date1); // 20
            String monthNumber = (String) DateFormat.format("MM", date1); // 06
            String year = (String) DateFormat.format("yyyy", date1); // 2013

            return new DateModel(day, monthNumber, year, date);

        } catch (Exception e) {
            return null;
        }
    }

    public static void showProgressDialog(Activity activity, String title) {
        checkDialog = new ProgressDialog(activity);
        checkDialog.setMessage(title);
        checkDialog.setIndeterminate(false);
        checkDialog.setCancelable(false);

        checkDialog.show();
    }

    public static void dismissProgressDialog() {
        checkDialog.dismiss();
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URL, Context context) {
        Glide.with(context).load(URL).into(imageView);
    }

    public static void customToast(Activity activity, String ToastTitle, boolean failed) {

        LayoutInflater inflater = activity.getLayoutInflater();

        int layout_id;

        if (failed) {
            layout_id = R.layout.toast;
        } else {
            layout_id = R.layout.success_toast;
        }

        View layout = inflater.inflate(layout_id, activity.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.text);
        text.setText(ToastTitle);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void setSpinner(Activity activity, Spinner spinner, List<String> names) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                R.layout.spinner_item_small, names);

        spinner.setAdapter(adapter);
    }

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);
            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
            return Imagebody;
        } else {
            return null;
        }
    }

    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), part);
                return requestBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void openGallery(Context context, ArrayList<AlbumFile> mAlbumFiles,
                                   Action<ArrayList<AlbumFile>> action, int count) {
        Album.initialize(AlbumConfig.newBuilder(context)
                .setAlbumLoader(new MediaLoader()).build());
        Album.image(context) // Image selection.
                .multipleChoice()
                .camera(true)
                .columnCount(3)
                .selectCount(count)
                .checkedList(mAlbumFiles)
                .onResult(action)
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                }).start();
    }

    public static void initRecyc() {
        List<OrderItem> list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        OrderAdapter orderAdapter = new OrderAdapter(activity, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderAdapter);
    }

    public static void acceptOrder(ApiServices apiServices, String apiToken, int orderId, Activity activity, String TAG) {
        showProgressDialog(activity, activity.getString(R.string.please_wait));
        apiServices.acceptRestaurantOrder(apiToken, orderId).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
                if (response.body().getStatus() == 1) {
                    customToast(activity, response.body().getMsg(), false);
                } else {
                    Log.d(TAG, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }
}


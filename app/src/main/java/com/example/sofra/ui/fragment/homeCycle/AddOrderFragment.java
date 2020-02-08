package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sofra.R;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.data.model.foodlist.Item;
import com.example.sofra.ui.BaseFragment;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.room.RoomManger.getInstance;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class AddOrderFragment extends BaseFragment {
    @BindView(R.id.fragment_order_iv_item_image)
    ImageView fragmentOrderIvItemImage;
    @BindView(R.id.fragment_order_tv_item_name)
    TextView fragmentOrderTvItemName;
    @BindView(R.id.fragment_order_tv_item_details)
    TextView fragmentOrderTvItemDetails;
    @BindView(R.id.fragment_order_tv_item_price)
    TextView fragmentOrderTvItemPrice;
    @BindView(R.id.fragment_order_ed_order)
    EditText fragmentOrderEdOrder;
    @BindView(R.id.fragment_order_tv_order_count)
    TextView fragmentOrderTvOrderCount;

    public Item item;
    private int quantity = 1;
    private String note;
    private RoomDao roomDao;
    private OrderItem orderItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        setData();
        roomDao = getInstance(getActivity()).roomDao();
        return view;
    }

    private void setData() {
        onLoadImageFromUrl(fragmentOrderIvItemImage, item.getPhotoUrl(), getActivity());
        fragmentOrderTvItemName.setText(item.getName());
        fragmentOrderTvItemDetails.setText(item.getDescription());
        fragmentOrderTvItemPrice.setText(item.getPrice());
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick({R.id.fragment_order_iv_remove_btn, R.id.fragment_order_iv_add_btn, R.id.fragment_order_iv_cart_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_order_iv_add_btn:
                quantity++;
                fragmentOrderTvOrderCount.setText(String.valueOf(quantity));
                break;
            case R.id.fragment_order_iv_remove_btn:
                if (quantity > 1) {
                    quantity--;
                    fragmentOrderTvOrderCount.setText(String.valueOf(quantity));
                }
                break;
            case R.id.fragment_order_iv_cart_btn:
                note = fragmentOrderEdOrder.getText().toString();
                Executors.newSingleThreadExecutor().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                orderItem = new OrderItem(item.getId(), Integer.parseInt(item.getRestaurantId()), Double.parseDouble(item.getPrice()),
                                        quantity, "ahmed", note, item.getPhotoUrl(),
                                        item.getName());
                                roomDao.addItem(orderItem);
                            }
                        });
                customToast(getActivity(), getString(R.string.added_to_store), false);
                break;
        }
    }
}

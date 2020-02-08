package com.example.sofra.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sofra.R;
import com.example.sofra.data.local.room.OrderItem;
import com.example.sofra.data.local.room.RoomDao;
import com.example.sofra.ui.fragment.homeCycle.CartFragment;
import com.example.sofra.ui.fragment.homeCycle.ClientMoreFragment;
import com.example.sofra.ui.fragment.homeCycle.ClientOrderContainerFragment;
import com.example.sofra.ui.fragment.homeCycle.EditClientProfileFragment;
import com.example.sofra.ui.fragment.homeCycle.NotificationFragment;
import com.example.sofra.ui.fragment.homeCycle.RestaurantListFragment;
import com.example.sofra.ui.fragment.homeCycle.EditRestaurantProfileFragment;
import com.example.sofra.ui.fragment.homeCycle.CommissionFragment;
import com.example.sofra.ui.fragment.homeCycle.RestaurantCategoriesFragment;
import com.example.sofra.ui.fragment.homeCycle.RestaurantMoreFragment;
import com.example.sofra.ui.fragment.homeCycle.RestaurantOrderContainerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.data.local.SofraConstans.USER_TYPE;
import static com.example.sofra.data.local.room.RoomManger.getInstance;
import static com.example.sofra.helper.HelperMethod.customToast;
import static com.example.sofra.helper.HelperMethod.replace;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.activity_home_bottom_navigation)
    public BottomNavigationView bottomNavigationView;
    @BindView(R.id.activity_home_tv_title)
    public TextView activityHomeTvTitle;
    @BindView(R.id.activity_home_iv_cart)
    ImageView activityHomeIvCart;

    String userType;
    RestaurantListFragment restaurantListFragment;
    ClientOrderContainerFragment clientOrderContainerFragment;
    EditClientProfileFragment editClientProfileFragment;
    ClientMoreFragment clientMoreFragment;

    RestaurantCategoriesFragment restaurantCategoriesFragment;
    RestaurantMoreFragment restaurantMoreFragment;
    RestaurantOrderContainerFragment restaurantOrderContainerFragment;
    EditRestaurantProfileFragment editRestaurantProfileFragment;

    CartFragment cartFragment;
    RoomDao roomDao;
    List<OrderItem> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        roomDao = getInstance(HomeActivity.this).roomDao();
        userType = LoadData(this, USER_TYPE);

        restaurantListFragment = new RestaurantListFragment();
        clientOrderContainerFragment = new ClientOrderContainerFragment();
        editClientProfileFragment = new EditClientProfileFragment();
        clientMoreFragment = new ClientMoreFragment();
        cartFragment = new CartFragment();
        restaurantCategoriesFragment = new RestaurantCategoriesFragment();
        restaurantMoreFragment = new RestaurantMoreFragment();
        restaurantOrderContainerFragment = new RestaurantOrderContainerFragment();
        editRestaurantProfileFragment = new EditRestaurantProfileFragment();

        if (userType.equals("client")) {
            activityHomeIvCart.setImageResource(R.drawable.ic_shopping);
            replace(restaurantListFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
        } else if (userType.equals("restaurant")) {
            activityHomeIvCart.setImageResource(R.drawable.ic_calculator);
            replace(restaurantCategoriesFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (userType.equals("client")) {
                    replace(restaurantListFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                } else if (userType.equals("restaurant")) {
                    replace(restaurantCategoriesFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }
                break;
            case R.id.check_list:
                if (userType.equals("client")) {
                    replace(clientOrderContainerFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                } else if (userType.equals("restaurant")) {
                    replace(restaurantOrderContainerFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }
                break;
            case R.id.profile:
                if (userType.equals("client")) {
                    replace(editClientProfileFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                } else if (userType.equals("restaurant")) {
                    replace(editRestaurantProfileFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }
                break;
            case R.id.more:
                if (userType.equals("client")) {
                    replace(clientMoreFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                } else if (userType.equals("restaurant")) {
                    replace(restaurantMoreFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }
                break;
        }
        return true;
    }

    @OnClick({R.id.activity_home_iv_notification, R.id.activity_home_iv_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_home_iv_notification:
                NotificationFragment notificationFragment = new NotificationFragment();
                replace(notificationFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                break;
            case R.id.activity_home_iv_cart:
                if (userType.equals("client")) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            ordersList = roomDao.getAll();
                            cartFragment.ordersList = ordersList;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    replace(cartFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                                }
                            });
                        }
                    });
                } else if (userType.equals("restaurant")) {
                    CommissionFragment commissionFragment = new CommissionFragment();
                    replace(commissionFragment, getSupportFragmentManager(), R.id.activity_home_fl_frame, null, null);
                }

                break;
        }
    }
}

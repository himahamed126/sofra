package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerAdapter;
import com.example.sofra.ui.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantOrderContainerFragment extends BaseFragment {
    @BindView(R.id.fragment_client_order_container_tl_tablayout)
    TabLayout fragmentClientOrderContainerTlTablayout;
    @BindView(R.id.fragment_client_order_container_vp_viewpager)
    ViewPager fragmentClientOrderContainerVpViewpager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        View view = inflater.inflate(R.layout.fragment_order_container, container, false);
        ButterKnife.bind(this, view);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        RestaurantOrderPendingFragment restaurantOrderPendingFragment = new RestaurantOrderPendingFragment();
        RestaurantOrderCurrentFragment restaurantOrderCurrentFragment = new RestaurantOrderCurrentFragment();
        RestaurantOrderCompleteFragment restaurantOrderCompleteFragment = new RestaurantOrderCompleteFragment();

        pagerAdapter.addPager(restaurantOrderPendingFragment, getString(R.string.new_requests));
        pagerAdapter.addPager(restaurantOrderCurrentFragment, getString(R.string.current_requests));
        pagerAdapter.addPager(restaurantOrderCompleteFragment, getString(R.string.old_requests));

        fragmentClientOrderContainerVpViewpager.setAdapter(pagerAdapter);
        fragmentClientOrderContainerTlTablayout.setupWithViewPager(fragmentClientOrderContainerVpViewpager);
        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}

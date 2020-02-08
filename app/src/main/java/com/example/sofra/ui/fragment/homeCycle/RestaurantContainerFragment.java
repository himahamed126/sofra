package com.example.sofra.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.sofra.R;
import com.example.sofra.adapter.ViewPagerAdapter;
import com.example.sofra.data.model.restaurant.Restaurant;
import com.example.sofra.helper.HelperMethod;
import com.example.sofra.ui.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class RestaurantContainerFragment extends BaseFragment {
    @BindView(R.id.fragment_restaurant_container_tl_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_restaurant_container_vp_viewpager)
    ViewPager viewPager;
    @BindView(R.id.fragment_restaurant_container_ly_top)
    RelativeLayout fragmentRestaurantContainerLyTop;
    @BindView(R.id.fragment_restaurant_container_iv_restaurant_avatar)
    CircleImageView fragmentRestaurantContainerIvRestaurantAvatar;
    @BindView(R.id.fragment_restaurant_container_tv_restaurant_name)
    TextView fragmentRestaurantContainerTvRestaurantName;

    public Restaurant data;
    private String TAG = "id";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        setupHomeActivity();
        View view = inflater.inflate(R.layout.fragment_restaurant_container, container, false);
        ButterKnife.bind(this, view);

        RestaurantItemFoodListFragment restaurantItemFoodListFragment = new RestaurantItemFoodListFragment();
        RestaurantReviewsFragment restaurantReviewsFragment = new RestaurantReviewsFragment();
        RestaurantInfoFragment restaurantInfoFragment = new RestaurantInfoFragment();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addPager(restaurantItemFoodListFragment, "قائمة الطعام");
        pagerAdapter.addPager(restaurantReviewsFragment, "التعليقات والتقييم");
        pagerAdapter.addPager(restaurantInfoFragment, "معلومات المتجر");


        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        onLoadImageFromUrl(fragmentRestaurantContainerIvRestaurantAvatar, data.getPhotoUrl(), getActivity());
        fragmentRestaurantContainerTvRestaurantName.setText(data.getName());

        restaurantItemFoodListFragment.restaurantData = data;
        restaurantReviewsFragment.restaurantDate = data;
        restaurantInfoFragment.restaurantData = data;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fragmentRestaurantContainerLyTop.setVisibility(View.GONE);
                        break;
                    case 1:
                    case 2:
                        fragmentRestaurantContainerLyTop.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }


    @Override
    public void onBack() {
        super.onBack();
    }
}

package com.dyadav.chirpntweet.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dyadav.chirpntweet.R;
import com.dyadav.chirpntweet.databinding.ActivityProfileBinding;
import com.dyadav.chirpntweet.fragments.FavoritesFragment;
import com.dyadav.chirpntweet.fragments.PhotosFragment;
import com.dyadav.chirpntweet.fragments.UserTimelineFragment;
import com.dyadav.chirpntweet.modal.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        //Get user info
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Backdrop
        Glide.with(this)
                .load(user.getCoverImageURL())
                .into(binding.backdrop);

        //Profile image
        Glide.with(this)
                .load(user.getProfileImageURL())
                .into(binding.profileImage);

        binding.followerCount.setText(user.getFollowerCount() + " FOLLOWERS");
        binding.followingCount.setText(user.getFollowingCount() + " FOLLOWING");
        binding.userName.setText(user.getName());
        binding.screenName.setText(user.getScreenName());
        if (null != user.getDescription())
            binding.description.setText(user.getDescription());

        if (null != user.getLocation())
            binding.location.setText(user.getLocation());

        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);

        binding.followerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "follower");
                startActivity(intent);
            }
        });

        binding.followingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "following");
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserTimelineFragment(), "TWEETS");
        adapter.addFragment(new PhotosFragment(), "PHOTOS");
        adapter.addFragment(new FavoritesFragment(), "FAVORITES");
        viewPager.setAdapter(adapter);
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ProfilePagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            mFragmentList.get(position).setArguments(bundle);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

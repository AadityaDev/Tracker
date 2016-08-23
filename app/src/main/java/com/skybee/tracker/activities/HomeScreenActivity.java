package com.skybee.tracker.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.skybee.tracker.GPSTracker;
import com.skybee.tracker.R;
import com.skybee.tracker.core.BaseActivity;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.customview.navigationtabbar.ntb.NavigationTabBar;
import com.skybee.tracker.ui.fragments.AdminFeed;
import com.skybee.tracker.ui.fragments.Home;
import com.skybee.tracker.ui.fragments.Map;
import com.skybee.tracker.ui.fragments.Profile;
import com.skybee.tracker.ui.fragments.Roasters;
import com.skybee.tracker.ui.fragments.Setting;

import java.util.ArrayList;

public class HomeScreenActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private UserStore userStore;
    private TextView selectAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        userStore = new UserStore(getApplicationContext());
        initializeUIComponents();
        selectAll=(TextView)findViewById(R.id.select_all_text);
        selectAll.setVisibility(View.INVISIBLE);
    }

    private void initializeUIComponents() {
        // Create the adapter that will return a fragment for each section
        if (userStore.getUserDetails().isAdmin()) {
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        new AdminFeed(),
                        new Map(),
                        new Profile(),
                        new Setting(),
                };
                private final String[] mFragmentNames = new String[]{
                        "Admin",
                        "Map",
                        "Profile",
                        "Settings"
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mPagerAdapter);

            final String[] colors = getResources().getStringArray(R.array.default_preview);
            final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
            final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.ic_launcher),
                            Color.parseColor(colors[0]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_place_24dp),
                            Color.parseColor(colors[1]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_emoticon_24dp),
                            Color.parseColor(colors[2]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_settings_24dp),
                            Color.parseColor(colors[3]))
                            .build()
            );
            navigationTabBar.setModels(models);
            navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(final int position) {
                    navigationTabBar.getModels().get(position).hideBadge();
                }

                @Override
                public void onPageScrollStateChanged(final int state) {

                }
            });
            navigationTabBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                        final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                        navigationTabBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                model.showBadge();
                            }
                        }, i * 100);
                    }
                }
            }, 500);
            navigationTabBar.setViewPager(mViewPager, 0);
        } else {
            mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                private final Fragment[] mFragments = new Fragment[]{
                        new Home(),
                        new Map(),
                        new Roasters(),
                        new Profile(),
                        new Setting(),
                };
                private final String[] mFragmentNames = new String[]{
                        "Home",
                        "Map",
                        "Roasters",
                        "Profile",
                        "Settings"
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mPagerAdapter);

            final String[] colors = getResources().getStringArray(R.array.default_preview);
            final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
            final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.ic_launcher),
                            Color.parseColor(colors[0]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_place_24dp),
                            Color.parseColor(colors[1]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_date_range),
                            Color.parseColor(colors[2]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_emoticon_24dp),
                            Color.parseColor(colors[2]))
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.drawable.ic_settings_24dp),
                            Color.parseColor(colors[3]))
                            .build()
            );
            navigationTabBar.setModels(models);
            navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(final int position) {
                    navigationTabBar.getModels().get(position).hideBadge();
                }

                @Override
                public void onPageScrollStateChanged(final int state) {

                }
            });
            navigationTabBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                        final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                        navigationTabBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                model.showBadge();
                            }
                        }, i * 100);
                    }
                }
            }, 500);
            navigationTabBar.setViewPager(mViewPager, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

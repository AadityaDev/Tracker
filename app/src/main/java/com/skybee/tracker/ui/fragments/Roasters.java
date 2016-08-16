package com.skybee.tracker.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skybee.tracker.R;
import com.skybee.tracker.core.BaseFragment;
import com.skybee.tracker.model.User;
import com.skybee.tracker.preferences.UserStore;
import com.skybee.tracker.ui.customview.SlidingTabLayout;

public class Roasters extends BaseFragment {

    private String TAG=this.getClass().getSimpleName();
    private FragmentTabHost fragmentTabHost;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    CharSequence Titles[] = {"ROASTER", "ACCEPTED", "REJECTED"};
    int NumbOfTabs = 3;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UserStore userStore = new UserStore(getContext());
        user = new User();
        user = userStore.getUserDetails();
        View view = inflater.inflate(R.layout.fragment_rosters, container, false);
        //creating view pager adapter
//        adapter = new ViewPagerAdapter(getFragmentManager(), Titles, NumbOfTabs);
//        //Assign the viewpager view
//        pager = (ViewPager) view.findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//        //Assign sliding tab layout
//        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
//        tabs.setDistributeEvenly(true);
//        //setting custom color
//        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.colorPrimaryDark);
//            }
//        });
//        tabs.setViewPager(pager);

        fragmentTabHost=(FragmentTabHost)view.findViewById(R.id.tabhost);
        fragmentTabHost.setup(getContext(),getChildFragmentManager(),R.id.realtabcontent);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[0].toString()).setIndicator(Titles[0]),Roster.class,null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[1].toString()).setIndicator(Titles[1]),AcceptedRoaster.class,null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(Titles[2].toString()).setIndicator(Titles[2]),RejectedRoaster.class,null);
        return view;
    }

    public void roaster() {

    }

    public void roasterAccepted() {

    }

    public void roasterRejected() {

    }
}

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Roster roster =new Roster();
                return roster;
            case 1:

            case 2:
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}

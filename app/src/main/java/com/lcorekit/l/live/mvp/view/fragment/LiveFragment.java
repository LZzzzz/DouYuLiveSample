package com.lcorekit.l.live.mvp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.adapter.BaseFragmentAdapter;
import com.lcorekit.l.live.commons.Pages;
import com.lcorekit.l.live.mvp.view.fragment.impl.LiveFragmentView;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.CsGoFragment;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.Dota2Fragment;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.HearthStoneFragment;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.LoLFragment;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.StarCraftFragment;
import com.lcorekit.l.live.mvp.view.fragment.secondfragment.WoWFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有频道列表
 * Created by l on 16-12-31.
 */

public class LiveFragment extends Fragment implements LiveFragmentView {

    private static LiveFragment fragment = null;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tb_channels)
    TabLayout tbChannels;
    private View mView;
    private String[] mTitles;

    public static LiveFragment getInstance(int type) {
        Bundle args = new Bundle();
        fragment = new LiveFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_channals_list, container, false);
            ButterKnife.bind(this, mView);
        }
        initData();
        return mView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        tbChannels.setTabTextColors(Color.DKGRAY, getResources().getColor(R.color.colorPrimary));
        tbChannels.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tbChannels.setTabMode(TabLayout.MODE_SCROLLABLE);
        vpContent.setOffscreenPageLimit(3);
        mTitles = Pages.TITLES;
        //设置标题
        for (int i = 0; i < mTitles.length; i++) {
            tbChannels.addTab(tbChannels.newTab().setText(mTitles[i]));
        }
        setUpViewPager(vpContent);
        tbChannels.setupWithViewPager(vpContent);
    }

    /**
     * 设置ViewPager
     */
    @Override
    public void setUpViewPager(ViewPager mViewPager) {
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(LoLFragment.getInstance(Pages.LOL_PAGE), mTitles[0]);
        adapter.addFragment(HearthStoneFragment.getInstance(Pages.HEARTHSTONE_PAGE), mTitles[1]);
        adapter.addFragment(Dota2Fragment.getInstance(Pages.DOTA2_PAGE), mTitles[2]);
        adapter.addFragment(StarCraftFragment.getInstance(Pages.STARCRAFT_PAGE), mTitles[3]);
        adapter.addFragment(WoWFragment.getInstance(Pages.WOW_PAGE), mTitles[4]);
        adapter.addFragment(CsGoFragment.getInstance(Pages.CSGO_PAGE), mTitles[5]);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }
}

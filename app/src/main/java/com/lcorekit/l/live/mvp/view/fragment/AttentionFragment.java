package com.lcorekit.l.live.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.adapter.RoomInfoAdapter;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.db.bean.Person;
import com.lcorekit.l.live.mvp.presenter.AttentionPresenter;
import com.lcorekit.l.live.mvp.presenter.impl.IAttentionPresenter;
import com.lcorekit.l.live.mvp.view.activity.PalyActivity;
import com.lcorekit.l.live.mvp.view.fragment.impl.AttentionFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l on 16-12-31.
 */

public class AttentionFragment extends Fragment implements AttentionFragmentView {

    private static AttentionFragment fragment = null;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private View mView;
    private List<RoomInfo> roomInfos;
    private RoomInfoAdapter mAdapter;
    private IAttentionPresenter mAttentionPresenter;

    public static synchronized AttentionFragment getInstance(int type) {
        if (fragment == null) {
            Bundle args = new Bundle();
            fragment = new AttentionFragment();
            args.putInt("type", type);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_list, container, false);
            ButterKnife.bind(this, this.mView);
        }
        roomInfos = new ArrayList<>();
        mAttentionPresenter = new AttentionPresenter(this);
//        mAttentionPresenter.getAllRoomId();
        initView();
        setRefresh();
        setLoadmore();
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        roomInfos.clear();
        mAttentionPresenter.getAllRoomId();
    }

    @Override
    public void onStop() {
        super.onStop();
        roomInfos.clear();
    }

    @Override
    public void showData(List<Person> persons) {
        for (Person person : persons) {
            mAttentionPresenter.getRoomInfoResult(String.valueOf(person.getRoomId()));
        }
    }

    @Override
    public void initView() {
        mAdapter = new RoomInfoAdapter(getContext(), roomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        //第一列单独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        rlChannels.setLayoutManager(gridLayoutManager);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                //TODO：推荐待完成
                int roomId = roomInfos.get(i).roomId;
                String roomName = roomInfos.get(i).roomName;
                startPlay(roomName, roomId);
            }
        });
        rlChannels.setAdapter(mAdapter);

    }

    @Override
    public void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                roomInfos.clear();
                mAttentionPresenter.getAllRoomId();
            }
        });
    }

    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeToLoadComplete();
            }
        });
    }

    @Override
    public void swipeToLoadComplete() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void startPlay(String roomName, int roomId) {
        Intent intent = new Intent(getContext(), PalyActivity.class);
        intent.putExtra("name", roomName);
        intent.putExtra("id", roomId);
        startActivity(intent);
    }

    @Override
    public void getAllRoomInfo(List<RoomInfo> roomInfos) {
        this.roomInfos.addAll(roomInfos);
        mAdapter.setNewData(this.roomInfos);
        mAdapter.notifyDataSetChanged();
    }
}

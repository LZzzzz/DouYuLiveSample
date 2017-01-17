package com.lcorekit.l.live.mvp.view.base;

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
import com.lcorekit.l.live.mvp.presenter.ChannelListPresenter;
import com.lcorekit.l.live.mvp.presenter.impl.IChannelListPresenter;
import com.lcorekit.l.live.mvp.view.activity.PalyActivity;
import com.lcorekit.l.live.mvp.view.base.impl.ChannelListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 基类
 * Created by l on 16-12-30.
 */
public abstract class BaseFragment extends Fragment implements ChannelListView {

    private View mView;
    private IChannelListPresenter mChannelListPresenter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;

    private List<RoomInfo> roomInfos;
    private RoomInfoAdapter mAdapter;
    private int offset = 0;
    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_list, container, false);
            ButterKnife.bind(this, this.mView);
        }

        type = getType();
        roomInfos = new ArrayList<>();
        mChannelListPresenter = new ChannelListPresenter(this);
        mChannelListPresenter.getChannelList(type, offset);
        initView();
        setRefresh();
        setLoadmore();
        return mView;
    }

    public abstract int getType();

    @Override
    public void showData(List<RoomInfo> datas) {
        if (offset == 0) {
            roomInfos.clear();
            roomInfos.addAll(datas);
            mAdapter.notifyDataSetChanged();
            mAdapter.setNewData(datas);
        } else {
            roomInfos.addAll(datas);
            mAdapter.notifyDataSetChanged();
            mAdapter.addData(datas);
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
                int roomId = roomInfos.get(i).roomId;
                String roomName = roomInfos.get(i).roomName;
//                mChannelListPresenter.getPlayerInfo(roomId, roomName);
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
                offset = 0;//刷新页数置0
                mChannelListPresenter.getChannelList(type, offset);
            }
        });
    }

    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset += 20;
                mChannelListPresenter.getChannelList(type, offset);
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
}

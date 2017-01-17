package com.lcorekit.l.live.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.adapter.RoomInfoAdapter;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.mvp.presenter.ChannelInfoPreseter;
import com.lcorekit.l.live.mvp.presenter.impl.IChannelInfoPreseter;
import com.lcorekit.l.live.mvp.view.activity.impl.ChannelInfoListActivityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static com.lzy.okgo.OkGo.getContext;

/**
 * 每个类别的详情页
 * Created by l on 17-1-9.
 */

public class ChannelInfoListActivity extends SwipeBackActivity implements ChannelInfoListActivityView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private IChannelInfoPreseter mChannelInfoPreseter;
    private int channelTag;
    private String channelName;
    private int offset = 0;
    private RoomInfoAdapter mAdapter;
    private List<RoomInfo> roomInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        channelName = intent.getStringExtra("name");
        channelTag = intent.getIntExtra("tag", 0);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);
        roomInfos = new ArrayList<>();
        mChannelInfoPreseter = new ChannelInfoPreseter(this);
        mChannelInfoPreseter.getChannelInfoList(channelTag, offset);
        initView();
        setRefresh();
        setLoadmore();
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
//            ab.setTitle(channelName);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        // 结束当前界面，返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(channelName);
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
                startPlay(roomName, roomId);
            }
        });
        rlChannels.setAdapter(mAdapter);
    }

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
    public void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;//刷新页数置0
                mChannelInfoPreseter.getChannelInfoList(channelTag, offset);
            }
        });
    }

    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset += 20;
                mChannelInfoPreseter.getChannelInfoList(channelTag, offset);
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
        Intent intent = new Intent(this, PalyActivity.class);
        intent.putExtra("name", roomName);
        intent.putExtra("id", roomId);
        startActivity(intent);
    }
}

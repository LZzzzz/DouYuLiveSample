package com.lcorekit.l.live.mvp.view.base.impl;


import com.lcorekit.l.live.bean.RoomInfo;

import java.util.List;

/**
 * Created by l on 17-1-5.
 */
public interface ChannelListView {
    void showData(List<RoomInfo> datas);

    void initView();

    void setRefresh();

    void setLoadmore();

    void swipeToLoadComplete();

    void startPlay(String roomName, int roomId);
}

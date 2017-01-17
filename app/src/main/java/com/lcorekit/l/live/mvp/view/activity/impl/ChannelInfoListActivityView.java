package com.lcorekit.l.live.mvp.view.activity.impl;


import com.lcorekit.l.live.bean.RoomInfo;

import java.util.List;

/**
 * Created by l on 17-1-9.
 */
public interface ChannelInfoListActivityView {
    void initView();
    void showData(List<RoomInfo> datas);
    void setRefresh();
    void setLoadmore();
    void swipeToLoadComplete();
    void startPlay(String roomName, int roomId);
}

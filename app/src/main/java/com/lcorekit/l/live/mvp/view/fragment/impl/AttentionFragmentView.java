package com.lcorekit.l.live.mvp.view.fragment.impl;


import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.db.bean.Person;

import java.util.List;

/**
 * Created by l on 17-1-15.
 */
public interface AttentionFragmentView {

    void showData(List<Person> datas);

    void initView();

    void setRefresh();

    void setLoadmore();

    void swipeToLoadComplete();

    void startPlay(String roomName, int roomId);

    void getAllRoomInfo(List<RoomInfo> roomInfos);
}

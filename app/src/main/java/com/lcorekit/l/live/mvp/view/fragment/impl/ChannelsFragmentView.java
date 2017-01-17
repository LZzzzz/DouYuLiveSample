package com.lcorekit.l.live.mvp.view.fragment.impl;


import com.lcorekit.l.live.bean.SubChannelInfo;

import java.util.List;

/**
 * Created by l on 17-1-2.
 */
public interface ChannelsFragmentView {
    void showData(List<SubChannelInfo> datas);
    void initView();
    void setRefresh();
    void setLoadmore();
    void swipeToLoadComplete();
}

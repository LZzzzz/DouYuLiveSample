package com.lcorekit.l.live.mvp.model.impl;

import rx.Observable;

/**
 * Created by l on 17-1-2.
 */
public interface IDouYuModel {
    Observable<String> ChannelsGet();

    Observable<String> ChannelListGet(int channelid, int offset);

    Observable<String> RecommendGet(int offset);

    Observable<String> SearchGet(String keyword);

    Observable<String> PlyerInfoGet(int roomId);

    Observable<String> ChannelInfoGet(int tag, int offset);
}

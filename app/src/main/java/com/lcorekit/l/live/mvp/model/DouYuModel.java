package com.lcorekit.l.live.mvp.model;


import com.lcorekit.l.live.mvp.model.impl.IDouYuModel;
import com.lcorekit.l.live.utils.DouYuApi;
import com.lcorekit.l.live.utils.NumberUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okrx.RxAdapter;

import rx.Observable;

/**
 * 数据请求
 * Created by l on 16-12-31.
 */

public class DouYuModel implements IDouYuModel {
    public DouYuModel() {
    }

    /**
     * 所有类别
     *
     * @return Observable
     */
    @Override
    public Observable<String> ChannelsGet() {
        return OkGo.get(DouYuApi.getDouyuAllSubChannelsUrl())
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    /**
     * 频道列表
     */
    @Override
    public Observable<String> ChannelListGet(int channelid, int offset) {
        return OkGo.get(DouYuApi.getChannelUrl(channelid, offset))
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    /**
     * 推荐
     *
     * @return Observable
     */
    @Override
    public Observable<String> RecommendGet(int offset) {
        return OkGo.get(DouYuApi.getDouyuRecommendUrl(offset))
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    /**
     * 搜索
     */
    @Override
    public Observable<String> SearchGet(String keyword) {
        String url;
        //判断是关键字还是房间号
        if (NumberUtils.isNumeric(keyword)) {
            url = DouYuApi.getDouyuRoom(Integer.valueOf(keyword));
        } else {
            url = DouYuApi.getDouyuSearchUrl(keyword);
        }
        return OkGo.get(url)
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    /**
     * 获取房间播放信息
     *
     * @param roomId 房间号
     * @return Observable
     */
    @Override
    public Observable<String> PlyerInfoGet(int roomId) {
        return OkGo.post(DouYuApi.getDouyuRoomUrl(roomId))
                .params(DouYuApi.getDouyuRoomParams(roomId))
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }

    /**
     * 获取频道所有主播
     *
     * @param tag tag
     * @return Observable
     */
    @Override
    public Observable<String> ChannelInfoGet(int tag, int offset) {
        String douyuSubChannelBaseTag = DouYuApi.getDouyuSubChannelBaseTag(tag);
        return OkGo.get(DouYuApi.getChannelUrl(tag, offset))
                .getCall(StringConvert.create(), RxAdapter.<String>create());
    }


}

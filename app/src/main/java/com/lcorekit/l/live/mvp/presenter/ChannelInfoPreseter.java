package com.lcorekit.l.live.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.bean.SubChannel;
import com.lcorekit.l.live.mvp.model.DouYuModel;
import com.lcorekit.l.live.mvp.model.impl.IDouYuModel;
import com.lcorekit.l.live.mvp.presenter.impl.IChannelInfoPreseter;
import com.lcorekit.l.live.mvp.view.activity.impl.ChannelInfoListActivityView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by l on 17-1-9.
 */

public class ChannelInfoPreseter implements IChannelInfoPreseter {
    private ChannelInfoListActivityView view;
    private IDouYuModel model;

    public ChannelInfoPreseter(ChannelInfoListActivityView view) {
        this.view = view;
        model = new DouYuModel();
    }

    @Override
    public void getChannelInfoList(int tag, int offset) {
        model.ChannelInfoGet(tag, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getChannelInfoListSubscriber());
    }

    private Observer<? super String> getChannelInfoListSubscriber() {
        Subscriber<String> channelInfoListSubscriber;
        channelInfoListSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("RX", "onCompleted");
                //停止刷新
                view.swipeToLoadComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("RX", "error");
                //停止刷新
                view.swipeToLoadComplete();
            }

            @Override
            public void onNext(String s) {
                Log.i("RX-->result: ", s);
                List<RoomInfo> roomInfos = new ArrayList<>();
                try {
                    Gson gson = new Gson();
                    SubChannel douyuRoomInfo = gson.fromJson(s, SubChannel.class);
                    for (SubChannel.Room room : douyuRoomInfo.data) {
                        RoomInfo roomInfo = new RoomInfo();
                        roomInfo.roomId = room.room_id;
                        roomInfo.roomSrc = room.room_src;
                        Log.i("room_name", room.room_name);
                        roomInfo.nickname = room.nickname;
                        roomInfo.roomName = room.room_name;
                        roomInfo.online = room.online;
                        roomInfos.add(roomInfo);
                    }
                    view.showData(roomInfos);
                    unsubscribe();
                    view.swipeToLoadComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return channelInfoListSubscriber;
    }
}

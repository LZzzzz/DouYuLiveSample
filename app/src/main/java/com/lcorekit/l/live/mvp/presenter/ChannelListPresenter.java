package com.lcorekit.l.live.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.bean.SubChannel;
import com.lcorekit.l.live.mvp.model.DouYuModel;
import com.lcorekit.l.live.mvp.model.impl.IDouYuModel;
import com.lcorekit.l.live.mvp.presenter.impl.IChannelListPresenter;
import com.lcorekit.l.live.mvp.view.base.impl.ChannelListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 频道列表的Presenter
 * Created by l on 17-1-4.
 */

public class ChannelListPresenter implements IChannelListPresenter {
    private ChannelListView view;
    private IDouYuModel model;
//    private String roomName;
//    private int roomId;

    public ChannelListPresenter(ChannelListView view) {
        this.view = view;
        this.model = new DouYuModel();
    }

    @Override
    public void getChannelList(int channelid, int offset) {
        model.ChannelListGet(channelid, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getChannelListSubscriber());
    }

//    @Override
//    public void getPlayerInfo(int roomId, String roomName) {
//        this.roomName = roomName;
//        this.roomId = roomId;
//        model.PlyerInfoGet(roomId)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getPlayerInfoSubscriber());
//    }

    private Observer<? super String> getChannelListSubscriber() {
        Subscriber<String> channelListSubscriber;
        channelListSubscriber = new Subscriber<String>() {
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
        return channelListSubscriber;
    }


//    private Observer<? super String> getPlayerInfoSubscriber() {
//        Subscriber<String> playerInfoSubscriber;
//        playerInfoSubscriber = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.i("RX-->live result: ", s);
//                try {
//                    Gson gson = new Gson();
//                    DouyuRoom douyuRoom = gson.fromJson(s, DouyuRoom.class);
//                    String url = douyuRoom.data.rtmp_url + "/" + douyuRoom.data.rtmp_live;
//                    System.out.println(url);
//                    view.stratPlay(url, roomName, roomId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        return playerInfoSubscriber;
//    }
}

package com.lcorekit.l.live.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lcorekit.l.live.bean.DouyuRoomInfo;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.bean.SubChannel;
import com.lcorekit.l.live.mvp.model.DouYuModel;
import com.lcorekit.l.live.mvp.model.impl.IDouYuModel;
import com.lcorekit.l.live.mvp.presenter.impl.ISearchPresenter;
import com.lcorekit.l.live.mvp.view.activity.impl.SearchActivityView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by l on 17-1-6.
 */

public class SearchPresenter implements ISearchPresenter {
    private SearchActivityView view;
    private IDouYuModel model;

    public SearchPresenter(SearchActivityView view) {
        this.view = view;
        model = new DouYuModel();
    }

    @Override
    public void getSearchListResult(String keyword) {
        model.SearchGet(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchListSubscriber());
    }

    private Observer<? super String> getSearchListSubscriber() {
        Subscriber<String> searchListSubscriber;
        searchListSubscriber = new Subscriber<String>() {
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
                    DouyuRoomInfo douyuRoomInfo = gson.fromJson(s, DouyuRoomInfo.class);
                    for (SubChannel.Room room : douyuRoomInfo.data.room) {
                        RoomInfo roomInfo = new RoomInfo();
                        roomInfo.roomId = room.room_id;
                        if (room.room_src != null) {
                            roomInfo.roomSrc = room.room_src;
                        } else {
                            roomInfo.roomSrc = room.roomSrc;
                        }
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
        return searchListSubscriber;
    }


}

package com.lcorekit.l.live.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.lcorekit.l.live.bean.DouyuRoom;
import com.lcorekit.l.live.db.bean.Person;
import com.lcorekit.l.live.mvp.model.AttentionModel;
import com.lcorekit.l.live.mvp.model.DouYuModel;
import com.lcorekit.l.live.mvp.model.impl.IAttentionModel;
import com.lcorekit.l.live.mvp.model.impl.IDouYuModel;
import com.lcorekit.l.live.mvp.presenter.impl.IPlayPresenter;
import com.lcorekit.l.live.mvp.view.activity.impl.PalyActivityView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by l on 17-1-7.
 */

public class PlayPresenter implements IPlayPresenter {
    private PalyActivityView view;
    private IDouYuModel model;
    private IAttentionModel attentionModel;
    private Subscription mTimeSubscribe;
    private Subscription mHideSubscribe;
    private Subscription mDismissSubscribe;
    private Subscription mAttentionsubscribe;
    private Subscription mDeletesubscribe;

    public PlayPresenter(PalyActivityView view) {
        this.view = view;
        model = new DouYuModel();
        attentionModel = new AttentionModel();
    }

    /**
     * 定时获取系统时间
     */
    @Override
    public void getSystemTime() {
        mTimeSubscribe = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d("interval", "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("interval", "error");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        view.updateSystemTime();
                    }
                });
    }

    /**
     * 自动隐藏播放面板
     */
    @Override
    public void autoHidePanel() {
        mHideSubscribe = Observable.timer(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view.hidePanel();
                        mHideSubscribe.unsubscribe();
                    }
                });
    }

    /**
     * 获取播放信息
     *
     * @param roomId 房间号
     */
    @Override
    public void getPlayerInfo(int roomId) {
        model.PlyerInfoGet(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getPlayerInfoSubscriber());
    }

    /**
     * 关闭滑动手势弹出框
     */
    @Override
    public void dismissVolAlpha() {
        mDismissSubscribe = Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view.hideVolAlpha();
                        mDismissSubscribe.unsubscribe();
                    }
                });
    }

    /**
     * 展示关注图标
     *
     * @param person 主播
     */
    @Override
    public void showAttention(Person person) {
        mAttentionsubscribe = attentionModel.query(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        view.showAttention(aBoolean);
                        mAttentionsubscribe.unsubscribe();
                    }
                });
    }

    /**
     * 插入
     *
     * @param person 主播
     */
    @Override
    public void insertPerson(Person person) {
        attentionModel.insertData(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Person>() {
                    @Override
                    public void onCompleted() {
                        view.showAttention(true);
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(Person person) {

                    }
                });

//        new Action1<Person>() {
//            @Override
//            public void call(Person person) {
//                view.showAttention(true);
//                mAttentionsubscribe.unsubscribe();
//            }
//        }
    }

    /**
     * 删除
     *
     * @param person 主播
     */
    @Override
    public void deletePerson(Person person) {
        mDeletesubscribe = attentionModel.deleteData(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        view.showAttention(false);
                        mDeletesubscribe.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
//        new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                view.showAttention(false);
//                mDeletesubscribe.unsubscribe();
//            }
//        }


    }


    @Override
    public void unRigister() {
        mTimeSubscribe.unsubscribe();
    }

    private Subscriber<? super String> getPlayerInfoSubscriber() {
        Subscriber<String> playerInfoSubscriber;
        playerInfoSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("RX-->live result: ", s);
                try {
                    Gson gson = new Gson();
                    DouyuRoom douyuRoom = gson.fromJson(s, DouyuRoom.class);
                    String url = douyuRoom.data.rtmp_url + "/" + douyuRoom.data.rtmp_live;
                    System.out.println(url);
                    view.playVideo(url);
                    unsubscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return playerInfoSubscriber;
    }


}

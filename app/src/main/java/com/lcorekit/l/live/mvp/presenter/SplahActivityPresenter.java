package com.lcorekit.l.live.mvp.presenter;

import android.util.Log;

import com.lcorekit.l.live.mvp.presenter.impl.ISplahActivityPresenter;
import com.lcorekit.l.live.mvp.view.activity.impl.SplashActivityView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.lzy.okgo.utils.OkLogger.tag;

/**
 * Created by l on 16-12-31.
 */

public class SplahActivityPresenter implements ISplahActivityPresenter {
    private SplashActivityView view;
    private Subscription subscription;

    public SplahActivityPresenter(SplashActivityView view) {
        this.view = view;
    }

    @Override
    public void setDelay() {
        //不想有延迟，秒开时间可以设置为0
        subscription = Observable.timer(1500, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                view.enterApp();
            }
        });
    }

    @Override
    public void unRegister() {
        Log.e(tag, "是否已经反注册：" + subscription.isUnsubscribed() + "");
        //先判断是否已经反注册
        if (!subscription.isUnsubscribed()) {
            Log.e(tag, "进行反注册");
            subscription.unsubscribe();
            Log.e(tag, "是否已经反注册：" + subscription.isUnsubscribed() + "");
        }
    }
}

package com.lcorekit.l.live.mvp.view.activity.impl;

/**
 * Created by l on 17-1-7.
 */
public interface PalyActivityView {
    void showPanel();

    void hidePanel();

    void initData();

    void updateSystemTime();

    void playVideo(String url);

    void onVolumeSlide(float percent);

    void onBrightnessSlide(float percent);

    void hideVolAlpha();

    void playDanmu();

    void showAttention(boolean isAttention);
}

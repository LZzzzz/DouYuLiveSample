package com.lcorekit.l.live.mvp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.commons.PlayerManager;
import com.lcorekit.l.live.db.bean.Person;
import com.lcorekit.l.live.mvp.presenter.PlayPresenter;
import com.lcorekit.l.live.mvp.presenter.impl.IPlayPresenter;
import com.lcorekit.l.live.mvp.view.activity.impl.PalyActivityView;
import com.lcorekit.l.live.utils.DanmuProcess;
import com.lcorekit.l.live.utils.StringUtils;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * 播放器界面
 * Created by l on 17-1-7.
 */

public class PalyActivity extends AppCompatActivity implements PlayerManager.PlayerStateListener, PalyActivityView {
    @BindView(R.id.iv_video_back)
    ImageButton ivVideoBack;
    @BindView(R.id.ib_play)
    ImageButton ibPlay;
    @BindView(R.id.ib_follow)
    ImageButton ibFollow;
    @BindView(R.id.ib_danmu)
    ImageButton ibDanmu;
    @BindView(R.id.ll_bottom_panel)
    LinearLayout llBottomPanel;
    @BindView(R.id.rl_top_panel)
    RelativeLayout rlTopPanel;
    @BindView(R.id.tv_video_title)
    TextView tvVideoTitle;
    @BindView(R.id.tv_video_system_time)
    TextView tvVideoSystemTime;
    @BindView(R.id.iv_video_battery)
    ImageView ivVideoBattery;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_vol_alph)
    LinearLayout llVolAlph;
    @BindView(R.id.iv_vol_bright)
    ImageView ivVolBright;
    @BindView(R.id.animationIV)
    ImageView animationIV;
    @BindView(R.id.danmakuView)
    DanmakuView danmakuView;
    private MyBatteryReceiver receiver;
    private PlayerManager playerManager;
    private IPlayPresenter mPlayPresenter;
    private boolean isShowPanel = true;
    private boolean isPause = false;
    private boolean isShowDanmu = true;
    private boolean isAttention = false;
    private int bottomHeight;
    private int topHeight;
    private GestureDetector detector;
    private String name;
    private int id;
    private float startY;
    private int screenWidth;
    private int maxVolume;
    private int screenHeight;
    private AudioManager manager;
    private float mBrightness = -1f;
    private AnimationDrawable animationDrawable;
    private DanmuProcess mDanmuProcess;
    private Person person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getIntExtra("id", 0);
        mPlayPresenter = new PlayPresenter(this);
        //动态注册电量的广播接收者
        receiver = new MyBatteryReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        initPlayer();
        initData();
    }

    private void initPlayer() {
        playerManager = new PlayerManager(this);
        playerManager.setFullScreenOnly(true);
        playerManager.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        playerManager.playInFullScreen(true);
        playerManager.setPlayerStateListener(this);
    }

    @Override
    public void initData() {
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShowPanel) {
                    hidePanel();
                } else {
                    showPanel();
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        //测量宽度高度
        llBottomPanel.measure(0, 0);
        rlTopPanel.measure(0, 0);
        //获取测量高度
        bottomHeight = llBottomPanel.getMeasuredHeight();
        topHeight = rlTopPanel.getMeasuredHeight();
        llBottomPanel.setTranslationY(0);
        rlTopPanel.setTranslationY(0);
        //获取屏幕宽度 高度
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        animationIV.setBackgroundResource(R.drawable.loading_anim);
        animationDrawable = (AnimationDrawable) animationIV.getBackground();
        animationIV.setVisibility(View.VISIBLE);
        animationDrawable.start();

        tvVideoTitle.setText(name);
        person = new Person();
        person.setRoomId(id);
        person.setName(name);
        mPlayPresenter.showAttention(person);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的位置
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float temp = (startY - y) / screenHeight;
                if (x < screenWidth / 2) {
                    //如果是左半边屏幕 处理音量变化
                    if (Math.abs(temp) > 0.067f) {
                        onVolumeSlide(temp);
                        startY = y;
                    }
                    return true;
                } else {
                    onBrightnessSlide(temp);
                }
                break;
            case MotionEvent.ACTION_UP:
                mPlayPresenter.dismissVolAlpha();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onPlay() {
        animationDrawable.stop();
        animationIV.setVisibility(View.GONE);
        manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        playDanmu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayPresenter.autoHidePanel();
        mPlayPresenter.getSystemTime();
        mPlayPresenter.getPlayerInfo(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //因为url动态获取的所以，没法暂停只能销毁，只能重新请求新的url
        playerManager.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayPresenter.unRigister();
        unregisterReceiver(receiver);
        if (mDanmuProcess != null) {
            mDanmuProcess.finish();
            danmakuView.release();
        }
    }

    /**
     * 显示面板
     */
    @Override
    public void showPanel() {
        llBottomPanel.setTranslationY(0);
        rlTopPanel.setTranslationY(0);
        isShowPanel = true;
        mPlayPresenter.autoHidePanel();
    }

    /**
     * 隐藏面板
     */
    @Override
    public void hidePanel() {
        llBottomPanel.setTranslationY(bottomHeight);
        rlTopPanel.setTranslationY(-topHeight);
        isShowPanel = false;
    }

    /**
     * 隐藏声音亮度面板
     */
    @Override
    public void hideVolAlpha() {
        llVolAlph.setVisibility(View.GONE);
    }

    @Override
    public void playDanmu() {
        mDanmuProcess = new DanmuProcess(this, danmakuView, id);
        mDanmuProcess.start();
    }

    @Override
    public void showAttention(boolean isAttention) {
        this.isAttention = isAttention;
        if (isAttention) {
            ibFollow.setImageResource(R.drawable.followed);
        } else {
            ibFollow.setImageResource(R.drawable.video_follow_selector);
        }
    }

    @Override
    public void updateSystemTime() {
        //获取当前时间设置到textview上
        tvVideoSystemTime.setText(StringUtils.formatSystemTime());
    }

    @Override
    public void playVideo(String url) {
        playerManager.play(url);
    }

    /**
     * 滑动调节音量
     *
     * @param percent 百分比
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onVolumeSlide(float percent) {
        llVolAlph.setVisibility(View.VISIBLE);
        ivVolBright.setImageDrawable(getDrawable(R.drawable.vol));
        int currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        currentVolume = (int) (percent * maxVolume) + currentVolume;
        if (currentVolume > maxVolume) {
            currentVolume = maxVolume;
        } else if (currentVolume < 0) {
            currentVolume = 0;
        }
        // 变更声音
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        double vol = currentVolume * 1.0 / maxVolume;
        tvNum.setText(nt.format(vol));
    }

    /**
     * 滑动调节亮度
     *
     * @param percent 百分比
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBrightnessSlide(float percent) {
        llVolAlph.setVisibility(View.VISIBLE);
        ivVolBright.setImageDrawable(getDrawable(R.drawable.brightness));
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        float sb = mBrightness + percent;
        if (sb > 1.0f) {
            sb = 1.0f;
        } else if (sb < 0.01f) {
            sb = 0.01f;
        }
        lpa.screenBrightness = sb;
        getWindow().setAttributes(lpa);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        tvNum.setText(nt.format(sb));
    }

    @OnClick({R.id.iv_video_back, R.id.ib_play, R.id.ib_follow, R.id.ib_danmu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video_back:
                finish();
                break;
            case R.id.ib_play:
                if (isPause) {
                    ibPlay.setImageResource(R.drawable.video_pause_selector);
                    animationIV.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                    mPlayPresenter.getPlayerInfo(id);
                    isPause = false;
                } else {
                    ibPlay.setImageResource(R.drawable.video_play_selector);
                    playerManager.onDestroy();
                    isPause = true;
                }
                break;
            case R.id.ib_follow:
                if (isAttention) {
                    mPlayPresenter.deletePerson(person);
                    isAttention = false;
                } else {
                    mPlayPresenter.insertPerson(person);
                    isAttention = true;
                }

                break;
            case R.id.ib_danmu:
                if (isShowDanmu) {
                    ibDanmu.setImageResource(R.drawable.pad_play_opendanmu);
                    danmakuView.hide();
                    isShowDanmu = false;
                } else {
                    ibDanmu.setImageResource(R.drawable.pad_play_closedanmu);
                    danmakuView.show();
                    isShowDanmu = true;
                }
                break;
        }
    }

    private class MyBatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //系统的电量是通过广播 以一个int类型的参数进行传递 对应的key level 数据在intent中进行携带
            int level = intent.getIntExtra("level", 100);
            if (level > 90) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_100);
            } else if (level > 80) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_80);
            } else if (level > 60) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_60);
            } else if (level > 40) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_40);
            } else if (level > 20) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_20);
            } else if (level > 10) {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_10);
            } else {
                ivVideoBattery.setImageResource(R.drawable.ic_battery_0);
            }
        }
    }
}

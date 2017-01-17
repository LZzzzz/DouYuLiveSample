package com.lcorekit.l.live.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.App;
import com.lcorekit.l.live.mvp.view.activity.AboutActivity;
import com.lcorekit.l.live.mvp.view.fragment.impl.SettingFragmentView;
import com.lcorekit.l.live.utils.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by l on 16-12-31.
 */

public class SettingFragment extends Fragment implements SettingFragmentView {

    private static SettingFragment fragment = null;
    @BindView(R.id.rl_setting_clearcache)
    RelativeLayout rlSettingClearcache;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_setting_cache)
    TextView tvSettingCache;
    private View mView;

    public static synchronized SettingFragment getInstance(int type) {
        if (fragment == null) {
            Bundle args = new Bundle();
            fragment = new SettingFragment();
            args.putInt("type", type);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_setting, container, false);
            ButterKnife.bind(this, this.mView);
        }
        initData();
        return mView;
    }

    private void initData() {
        tvSettingCache.setText(FileUtils.getCacheSize());
    }

    @OnClick({R.id.rl_setting_clearcache, R.id.tv_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_clearcache:
                showDialog("清理缓存");
                break;
            case R.id.tv_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
        }
    }

    private void showDialog(CharSequence title) {
        final MaterialDialog dialog = new MaterialDialog(getActivity())
                .setTitle(title)
                .setMessage("是否清楚缓存")
                .setCanceledOnTouchOutside(true);
        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getACache().clear();
                tvSettingCache.setText(FileUtils.getCacheSize());
                Toast.makeText(getContext(), "缓存已清除", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}

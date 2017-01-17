package com.lcorekit.l.live.mvp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by l on 17-1-16.
 */

public class AboutActivity extends SwipeBackActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);
        }
        // 结束当前界面，返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("关于");
    }


    @OnClick(R.id.fab)
    public void onClick() {
        showDialog();
    }

    private void showDialog() {
        final MaterialDialog dialog = new MaterialDialog(this)
                .setTitle("给我点个赞吧～")
                .setMessage("github传送门(*^__^*)")
                .setCanceledOnTouchOutside(true);
        dialog.setPositiveButton("开车", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getACache().clear();
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

package com.lcorekit.l.live.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.adapter.RoomInfoAdapter;
import com.lcorekit.l.live.bean.RoomInfo;
import com.lcorekit.l.live.mvp.presenter.SearchPresenter;
import com.lcorekit.l.live.mvp.presenter.impl.ISearchPresenter;
import com.lcorekit.l.live.mvp.view.activity.impl.SearchActivityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static com.lzy.okgo.OkGo.getContext;

/**
 * Created by l on 17-1-5.
 */
public class SearchActivity extends SwipeBackActivity implements SearchActivityView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private List<RoomInfo> roomInfos;
    private RoomInfoAdapter mAdapter;
    private ISearchPresenter mSearchPresenter;
    private String keyword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        roomInfos = new ArrayList<>();
        mSearchPresenter = new SearchPresenter(this);
        initView();
        setRefresh();
        setLoadmore();
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        // 结束当前界面，返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hiddenKyBroad();
                    keyword = etSearch.getText().toString();
                    if (!keyword.isEmpty()) {
                        mSearchPresenter.getSearchListResult(keyword);
                    } else {
                        Toast.makeText(SearchActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    hiddenKyBroad();
                }
            }
        });
        mAdapter = new RoomInfoAdapter(getContext(), roomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        //第一列单独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        rlChannels.setLayoutManager(gridLayoutManager);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                //TODO：搜索待完成
                int roomId = roomInfos.get(i).roomId;
                String roomName = roomInfos.get(i).roomName;
                startPlay(roomName, roomId);
            }
        });
        rlChannels.setAdapter(mAdapter);
    }

    @Override
    public void hiddenKyBroad() {
        /**隐藏软键盘**/
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showData(List<RoomInfo> datas) {
        Log.i("RX-->result: ", datas.toString());
        roomInfos.clear();
        roomInfos.addAll(datas);
        mAdapter.setNewData(datas);
        mAdapter.notifyDataSetChanged();
//
    }

    @Override
    public void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!keyword.isEmpty()) {
                    mSearchPresenter.getSearchListResult(keyword);
                } else {
                    swipeToLoadComplete();
                }
            }
        });
    }

    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeToLoadComplete();
            }
        });
    }

    @Override
    public void swipeToLoadComplete() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void startPlay(String roomName, int roomId) {
        Intent intent = new Intent(this, PalyActivity.class);
        intent.putExtra("name", roomName);
        intent.putExtra("id", roomId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("chu wen ti le");
    }
}

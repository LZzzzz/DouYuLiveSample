package com.lcorekit.l.live.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcorekit.l.douyu.R;
import com.lcorekit.l.live.bean.SubChannelInfo;
import com.lcorekit.l.live.utils.LoadImage;

import java.util.List;


/**
 * Created by l on 17-1-2.
 */

public class SubChannelAdapter extends BaseQuickAdapter<SubChannelInfo> {
    private Context mCtx;
    private List<SubChannelInfo> mData;

    public SubChannelAdapter(Context context, List<SubChannelInfo> data) {
        super(context, R.layout.channels_item, data);
        this.mCtx = context;
        this.mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SubChannelInfo subChannelInfo) {
        baseViewHolder.setText(R.id.tv_channels_name, subChannelInfo.tagName);
        ImageView channels_icon = baseViewHolder.getView(R.id.iv_channels);
        LoadImage.loadCircleImage(mCtx, subChannelInfo.iconUrl, channels_icon);
    }
}

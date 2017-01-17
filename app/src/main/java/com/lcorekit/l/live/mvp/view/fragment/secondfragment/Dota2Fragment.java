package com.lcorekit.l.live.mvp.view.fragment.secondfragment;

import android.os.Bundle;

import com.lcorekit.l.live.mvp.view.base.BaseFragment;


/**
 * Created by l on 16-12-31.
 */

public class Dota2Fragment extends BaseFragment {

    private static Dota2Fragment fragment = null;

    public static Dota2Fragment getInstance(int type) {
        Bundle args = new Bundle();
        fragment = new Dota2Fragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getType() {
        Bundle arguments = fragment.getArguments();
        return arguments.getInt("type");
    }
}

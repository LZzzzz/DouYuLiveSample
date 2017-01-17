package com.lcorekit.l.live.bean;

/**
 * Created by mengshen on 2016/11/1.
 */

public class DouyuRoom {
    public int error;
    public Data data;


    public static class Data {
        public int room_id;
        public String rtmp_url;
        public String rtmp_live;
    }
}

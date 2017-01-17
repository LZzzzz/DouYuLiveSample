package com.lcorekit.l.live.bean;

import java.util.List;

/**
 * Created by l on 2017/01/02.
 */
public class DouyuRoomInfo {

    public int error;
    public Data data;

    public static class Data {
        public List<SubChannel.Room> room;
    }
//    public int error;
//
//    public Data data;
//
//    public static class Data {
//        public List<SubChannel.Room> room;
//    }

}

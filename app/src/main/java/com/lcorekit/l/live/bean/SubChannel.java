package com.lcorekit.l.live.bean;

import java.util.List;

/**
 * Created by l on 2017/01/02.
 */

public class SubChannel {
    public List<Room> data;

    public static class Room {

        public int room_id;
        public String room_src;
        public String roomSrc;
        public String room_name;
        public String nickname;
        public int online;
    }
//    public List<Room> data;
//
//    public static class Room {
//        public int room_id;
//        public String room_src;
//        public String roomSrc;
//        public String room_name;
//        public String nick_name;
//        public int online;
//    }

}

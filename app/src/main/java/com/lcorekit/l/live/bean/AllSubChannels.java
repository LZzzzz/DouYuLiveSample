package com.lcorekit.l.live.bean;

import java.util.List;

/**
 * Created by l on 2016/12/31.
 */
public class AllSubChannels {
    public List<Data> data;

    public static class Data {
        public int tag_id;
        public String tag_name;
        public String icon_url;

        @Override
        public String toString() {
            return "Data [tag_id=" + tag_id + ", tag_name="
                    + tag_name + ", icon_url=" + icon_url + "]";
        }
    }

    @Override
    public String toString() {
        return "AllSubChannels [data=" + data + "]";
    }
}

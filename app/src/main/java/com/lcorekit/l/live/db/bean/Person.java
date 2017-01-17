package com.lcorekit.l.live.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by l on 17-1-14.
 */

@Entity
public class Person {
    @Id(autoincrement = true)
    private Long id;
    private long roomId;
    private String name;
    @Generated(hash = 837491630)
    public Person(Long id, long roomId, String name) {
        this.id = id;
        this.roomId = roomId;
        this.name = name;
    }
    @Generated(hash = 1024547259)
    public Person() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getRoomId() {
        return this.roomId;
    }
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }


}

package com.lcorekit.l.live.mvp.model;


import com.lcorekit.l.live.db.PersonDao;
import com.lcorekit.l.live.db.bean.Person;
import com.lcorekit.l.live.mvp.model.impl.IAttentionModel;
import com.lcorekit.l.live.utils.GreenDaoUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by l on 17-1-14.
 */

public class AttentionModel implements IAttentionModel {
    private PersonDao dao;

    public AttentionModel() {
        dao = GreenDaoUtils.getInstance().getmDaoSession().getPersonDao();
    }

    /**
     * 插入关注主播
     *
     * @param person 主播对象
     * @return Observable
     */
    @Override
    public Observable<Person> insertData(Person person) {
        return dao.rx().insert(person);
    }

    /**
     * 取消关注主播
     *
     * @param person 主播对象
     * @return Observable
     */
    @Override
    public Observable<Void> deleteData(final Person person) {
        return dao.queryBuilder().rx().list().map(new Func1<List<Person>, Void>() {
            @Override
            public Void call(List<Person> persons) {
                for (Person p : persons) {
                    if (p.getRoomId() == person.getRoomId()) {
                        dao.delete(p);
                    }
                }
                return null;
            }
        });
    }

    /**
     * 查询所有
     *
     * @return Observable
     */
    @Override
    public Observable<List<Person>> queryAll() {
        return dao.queryBuilder().rx().list();
    }

    /**
     * 查询一条
     *
     * @param person 主播
     * @return Observable
     */
    @Override
    public Observable<Boolean> query(final Person person) {
        return dao.queryBuilder().rx().list().exists(new Func1<List<Person>, Boolean>() {
            @Override
            public Boolean call(List<Person> persons) {
                for (Person p : persons) {
                    if (p.getRoomId() == person.getRoomId()) {
                        return true;
                    }
                }
                return false;
            }
        });
    }
}

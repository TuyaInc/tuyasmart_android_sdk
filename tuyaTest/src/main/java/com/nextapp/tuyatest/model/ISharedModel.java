package com.nextapp.tuyatest.model;


/**
 * Created by leaf on 15/12/21.
 * 共享数据
 */
public interface ISharedModel {
    void addMember(String mobile, String name, String code, String relation);

    void removeMember(String id);

    void getSentList();

    void getReceivedList();

    void updateMName(int id, String mname);
}

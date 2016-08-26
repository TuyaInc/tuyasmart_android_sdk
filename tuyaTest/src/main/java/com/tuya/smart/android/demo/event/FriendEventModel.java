package com.tuya.smart.android.demo.event;


import com.tuya.smart.android.user.bean.PersonBean;

/**
 * Created by letian on 15/6/2.
 */
public class FriendEventModel {
    public static final int OP_ADD = 0;
    public static final int OP_EDIT = 1;
    public static final int OP_EDIT_THIRD = 2;
    public static final int OP_QUERY = 3;

    private int operation;
    private PersonBean person;
    private int position;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public PersonBean getPerson() {
        return person;
    }

    public void setPerson(PersonBean person) {
        this.person = person;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}

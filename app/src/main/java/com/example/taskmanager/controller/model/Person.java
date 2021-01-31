package com.example.taskmanager.controller.model;

import com.example.taskmanager.controller.greenDao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "Person")
public class Person {

    @Id(autoincrement = true)
    private Long _id;

    @Unique
    @Property(nameInDb = "user_uuid")
    private String user_uuid;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID mID;

    @Property(nameInDb = "username")
    private String mUser;

    @Property(nameInDb = "password")
    private String mPass;


    private Date mDate;
    private int mTaskCount;

    public Person(String mUser, String mPass) {
        this.mID = UUID.randomUUID();
        this.mUser = mUser;
        this.mPass = mPass;
    }
    public Person(){
        this(UUID.randomUUID(), new Date());
    }
    public Person(UUID mID, Date mDate){
        this.mID = mID;
        this.mDate = mDate;
    }
    @Generated(hash = 1372558444)
    public Person(Long _id, String user_uuid, UUID mID, String mUser, String mPass,
            Date mDate, int mTaskCount) {
        this._id = _id;
        this.user_uuid = user_uuid;
        this.mID = mID;
        this.mUser = mUser;
        this.mPass = mPass;
        this.mDate = mDate;
        this.mTaskCount = mTaskCount;
    }


    public void setUser(String mUser) {
        this.mUser = mUser;
    }

    public void setPass(String mPass) {
        this.mPass = mPass;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public Long get_id() {
        return _id;
    }

    public UUID getID() {
        return mID;
    }

    public String getUser() {
        return mUser;
    }

    public String getPass() {
        return mPass;
    }

    public int getTaskCount() {
        return mTaskCount;
    }

    public void setTaskCount(int mTaskCount) {
        this.mTaskCount = mTaskCount;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getUser_uuid() {
        return this.user_uuid;
    }
    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }
    public UUID getMID() {
        return this.mID;
    }
    public void setMID(UUID mID) {
        this.mID = mID;
    }
    public String getMUser() {
        return this.mUser;
    }
    public void setMUser(String mUser) {
        this.mUser = mUser;
    }
    public String getMPass() {
        return this.mPass;
    }
    public void setMPass(String mPass) {
        this.mPass = mPass;
    }
    public Date getMDate() {
        return this.mDate;
    }
    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }
    public int getMTaskCount() {
        return this.mTaskCount;
    }
    public void setMTaskCount(int mTaskCount) {
        this.mTaskCount = mTaskCount;
    }
}

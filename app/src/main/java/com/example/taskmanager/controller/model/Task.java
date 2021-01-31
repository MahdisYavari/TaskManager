package com.example.taskmanager.controller.model;

import com.example.taskmanager.controller.greenDao.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity (nameInDb =  "Task")
public class Task {

    @Id (autoincrement = true)
    private Long _id;

    @Property (nameInDb =  "uuid")
    @Convert(converter = UuidConverter.class, columnType = String.class)
    @Index (unique = true)
    private UUID mUUID;

    @Property (nameInDb = "title")
    private String mTitle;

    @Property (nameInDb = "description")
    private String mDescription;

    @Property (nameInDb = "date")
    private Date mDate;

    @Property (nameInDb = "time")
    private Date mTime;

    @Property (nameInDb = "radioButton")
    private String mStateRadioButton;

    @Property (nameInDb = "StateViewpager")
    private String mStateViewPager;


    public Task(String mTitle, String mDescription, Date mDate, Date mTime, String mStateRadioButton, String mStateViewPager) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mStateRadioButton = mStateRadioButton;
        this.mStateViewPager = mStateViewPager;
    }

    public String getStateViewPager() {
        return mStateViewPager;
    }

    public void setStateViewPager(String mStateViewPager) {
        this.mStateViewPager = mStateViewPager;
    }

    public String getStateRadioButton() {
        return mStateRadioButton;
    }

    public void setStateRadioButton(String mStateRadioButton) {
        this.mStateRadioButton = mStateRadioButton;
    }


    public Task(){

      this(UUID.randomUUID());

    }
    public Task(UUID uuid){
        mUUID = uuid;

    }
    public String getPhotoName(){
        return "IMG_" + mUUID + ".jpg";
    }


    @Generated(hash = 1954185569)
    public Task(Long _id, UUID mUUID, String mTitle, String mDescription, Date mDate, Date mTime, String mStateRadioButton,
            String mStateViewPager) {
        this._id = _id;
        this.mUUID = mUUID;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mStateRadioButton = mStateRadioButton;
        this.mStateViewPager = mStateViewPager;
    }


    public UUID getUUID() {
        return mUUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }


    public Date getTime() {
        return mTime;
    }

    public void setTime(Date mTime) {
        this.mTime = mTime;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public UUID getMUUID() {
        return this.mUUID;
    }

    public void setMUUID(UUID mUUID) {
        this.mUUID = mUUID;
    }

    public String getMTitle() {
        return this.mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMDescription() {
        return this.mDescription;
    }

    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    public Date getMTime() {
        return this.mTime;
    }

    public void setMTime(Date mTime) {
        this.mTime = mTime;
    }

    public String getMStateRadioButton() {
        return this.mStateRadioButton;
    }

    public void setMStateRadioButton(String mStateRadioButton) {
        this.mStateRadioButton = mStateRadioButton;
    }

    public String getMStateViewPager() {
        return this.mStateViewPager;
    }

    public void setMStateViewPager(String mStateViewPager) {
        this.mStateViewPager = mStateViewPager;
    }
}

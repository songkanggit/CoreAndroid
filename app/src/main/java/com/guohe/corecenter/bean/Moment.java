package com.guohe.corecenter.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Moment implements Parcelable {
    private String id;
    private String accountId;
    private String accountName;
    private String accountHeadImage;
    private String content;
    private String[] imageList;
    private String likes;
    private Date createTime;

    public static Creator<Moment> CREATOR = new ClassLoaderCreator<Moment>() {
        @Override
        public Moment createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Moment(parcel);
        }

        @Override
        public Moment createFromParcel(Parcel parcel) {
            return new Moment(parcel);
        }

        @Override
        public Moment[] newArray(int i) {
            return new Moment[i];
        }
    };

    public Moment() { }

    public Moment(Parcel parcel) {
        id = parcel.readString();
        accountId = parcel.readString();
        accountName = parcel.readString();
        accountHeadImage = parcel.readString();
        content = parcel.readString();
        imageList = parcel.createStringArray();
        likes = parcel.readString();
        createTime = (Date) parcel.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(accountId);
        parcel.writeString(accountName);
        parcel.writeString(accountHeadImage);
        parcel.writeString(content);
        parcel.writeStringArray(imageList);
        parcel.writeString(likes);
        parcel.writeSerializable(createTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountHeadImage() {
        return accountHeadImage;
    }

    public void setAccountHeadImage(String accountHeadImage) {
        this.accountHeadImage = accountHeadImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

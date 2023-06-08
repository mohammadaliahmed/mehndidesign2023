package com.zik.mehndi.simple.offline.designs.Models;

import java.util.HashMap;

public class UserModel {
    String id,deviceId, name,gender,about,picUrl,fcmKey;
    long time;
    boolean blurPic;
    HashMap<String, String> friends, blockedMe, iBlocked;

    int reportCount;
    public UserModel() {
    }

    public UserModel(String id, String deviceId,String name, String gender,String about, String picUrl,
                     long time,boolean blurPic) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.gender = gender;

        this.picUrl = picUrl;
        this.about = about;
        this.time = time;
        this.blurPic = blurPic;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public int getReportCount() {
        return reportCount;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public HashMap<String, String> getBlockedMe() {
        return blockedMe;
    }

    public void setBlockedMe(HashMap<String, String> blockedMe) {
        this.blockedMe = blockedMe;
    }

    public HashMap<String, String> getiBlocked() {
        return iBlocked;
    }

    public void setiBlocked(HashMap<String, String> iBlocked) {
        this.iBlocked = iBlocked;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isBlurPic() {
        return blurPic;
    }

    public void setBlurPic(boolean blurPic) {
        this.blurPic = blurPic;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

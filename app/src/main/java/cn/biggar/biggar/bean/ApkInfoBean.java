package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by langgu on 16/5/19.
 */
public class ApkInfoBean implements Serializable{

    private String downloadUrl;  //下载地址
    private String versionName;  //apk版本
    private String size;    //apk文件大小
    private int versionCode;   //apk版本号(更新必备)
    private String name;  //apk名字
    private String updateLog;   //apk更新日志
    private String isMust; // 是否必须更新
    private long UpdateDate;
    public String isLoad;



    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String isMust() {
        return isMust;
    }

    public void setMust(String must) {
        isMust = must;
    }

    public long getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(long updateDate) {
        UpdateDate = updateDate;
    }
}

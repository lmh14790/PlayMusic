package com.yztc.bean;

/**
 * Created by Administrator on 2016/7/1.
 */
public class MusicBean {
    private String FileName;

    @Override
    public String toString() {
        return "MusicBean{" +
                "FileName='" + FileName + '\'' +
                ", path='" + path + '\'' +
                ", time=" + time +
                '}';
    }

    public MusicBean(String fileName, String path) {
        FileName = fileName;
        this.path = path;
    }
    public MusicBean() {
    }
    private String path;
    private Integer time;

    public MusicBean(String fileName, String path, Integer time) {
        FileName = fileName;
        this.path = path;
        this.time = time;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

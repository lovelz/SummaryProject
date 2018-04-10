package com.hao.summaryproject.download.bean;

import java.io.Serializable;

/**
 * @author lovelz
 * @date on 2018/4/4.
 */

public class FileInfo implements Serializable {

    private int id;
    private int length;
    private int finished;
    private String fileName;
    private String url;
    private String version;

    public FileInfo(int id, int length, int finished, String fileName, String url, String version) {
        this.id = id;
        this.length = length;
        this.finished = finished;
        this.fileName = fileName;
        this.url = url;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

package com.junl.wpwx.model;

import java.io.Serializable;
import java.util.List;

public class PoData implements Serializable {

    private String id;
    private String title;
    private List<String> image;
    private String keywords;
    private String type;//   "0":没有图片 1：有一张图片 2：有多张图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



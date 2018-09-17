package com.junl.wpwx.model;


import org.apache.ibatis.type.Alias;

@Alias("PromotMessage")
public class PromotMessage  {

    private String id; //
    private String title; // 标题
    private String link; //链接
    private String image; //图片
    private String description; // 描述
    private String content; // 内容


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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

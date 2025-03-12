package com.example.entity;


/**
 * 消息通知
 */
public class Message {
    /** ID */
    private Integer id;
    /** 消息内容 */
    private String content;
    /** 用户ID */
    private Integer userId;
    /** 是否已读 */
    private String isread;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

}
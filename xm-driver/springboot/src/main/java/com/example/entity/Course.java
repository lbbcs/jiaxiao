package com.example.entity;


/**
 * 课程信息
 */
public class Course {
    /** ID */
    private Integer id;
    /** 课程名称 */
    private String name;
    /** 课程介绍 */
    private String descr;
    /** 课程类型 */
    private String type;
    /** 课程时长 */
    private Integer during;
    /** 上课时间 */
    private String time;
    /** 上课地点 */
    private String location;
    /** 教练ID */
    private Integer coachId;
    /** 最大人数 */
    private Integer max;
    /** 课程状态 */
    private String status;
    /** 上架状态 */
    private String listing;
    private String coachName;
    private Boolean userReserve;

    public Boolean getUserReserve() {
        return userReserve;
    }

    public void setUserReserve(Boolean userReserve) {
        this.userReserve = userReserve;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuring() {
        return during;
    }

    public void setDuring(Integer during) {
        this.during = during;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCoachId() {
        return coachId;
    }

    public void setCoachId(Integer coachId) {
        this.coachId = coachId;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

}
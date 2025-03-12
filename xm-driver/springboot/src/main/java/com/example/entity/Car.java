package com.example.entity;


/**
 * 车辆信息
 */
public class Car {
    /** ID */
    private Integer id;
    /** 车辆名称 */
    private String name;
    /** 车辆照片 */
    private String img;
    /** 车辆品牌 */
    private String brand;
    /** 车辆型号 */
    private String no;
    /** 车辆颜色 */
    private String color;
    /** 座位数 */
    private Integer seats;
    /** 状态 */
    private String status;
    /** 购置日期 */
    private String date;
    /** 上次保养日期 */
    private String serviceDate;

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

}
package com.example.mapper;

import com.example.entity.CarReserve;
import java.util.List;

/**
 * 操作car_reserve相关数据接口
 */
public interface CarReserveMapper {

    /**
     * 新增
     */
    int insert(CarReserve carReserve);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(CarReserve carReserve);

    /**
     * 根据ID查询
     */
    CarReserve selectById(Integer id);

    /**
     * 查询所有
     */
    List<CarReserve> selectAll(CarReserve carReserve);

}
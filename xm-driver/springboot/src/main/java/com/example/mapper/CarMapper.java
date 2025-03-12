package com.example.mapper;

import com.example.entity.Car;
import java.util.List;

/**
 * 操作car相关数据接口
 */
public interface CarMapper {

    /**
     * 新增
     */
    int insert(Car car);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Car car);

    /**
     * 根据ID查询
     */
    Car selectById(Integer id);

    /**
     * 查询所有
     */
    List<Car> selectAll(Car car);

}
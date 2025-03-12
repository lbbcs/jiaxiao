package com.example.service;

import com.example.entity.Car;
import com.example.mapper.CarMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 车辆信息业务处理
 **/
@Service
public class CarService {

    @Resource
    private CarMapper carMapper;

    /**
     * 新增
     */
    public void add(Car car) {
        carMapper.insert(car);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        carMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            carMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Car car) {
        carMapper.updateById(car);
    }

    /**
     * 根据ID查询
     */
    public Car selectById(Integer id) {
        return carMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Car> selectAll(Car car) {
        return carMapper.selectAll(car);
    }

    /**
     * 分页查询
     */
    public PageInfo<Car> selectPage(Car car, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Car> list = carMapper.selectAll(car);
        return PageInfo.of(list);
    }

}
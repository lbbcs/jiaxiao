package com.example.controller;

import com.example.common.Result;
import com.example.entity.Car;
import com.example.service.CarService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆信息前端操作接口
 **/
@RestController
@RequestMapping("/car")
public class CarController {

    @Resource
    private CarService carService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Car car) {
        carService.add(car);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        carService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Car car) {
        carService.updateById(car);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Car car = carService.selectById(id);
        return Result.success(car);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Car car) {
        List<Car> list = carService.selectAll(car);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Car car,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Car> page = carService.selectPage(car, pageNum, pageSize);
        return Result.success(page);
    }

}
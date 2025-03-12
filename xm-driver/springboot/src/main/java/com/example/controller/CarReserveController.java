package com.example.controller;

import com.example.common.Result;
import com.example.entity.CarReserve;
import com.example.service.CarReserveService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 车辆预约前端操作接口
 **/
@RestController
@RequestMapping("/carReserve")
public class CarReserveController {

    @Resource
    private CarReserveService carReserveService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody CarReserve carReserve) {
        carReserveService.add(carReserve);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        carReserveService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carReserveService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody CarReserve carReserve) {
        carReserveService.updateById(carReserve);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        CarReserve carReserve = carReserveService.selectById(id);
        return Result.success(carReserve);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(CarReserve carReserve) {
        List<CarReserve> list = carReserveService.selectAll(carReserve);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(CarReserve carReserve,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<CarReserve> page = carReserveService.selectPage(carReserve, pageNum, pageSize);
        return Result.success(page);
    }

}
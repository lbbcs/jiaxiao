package com.example.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.CarReserve;
import com.example.entity.Reserve;
import com.example.exception.CustomException;
import com.example.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    UserService userService;
    @Resource
    CoachService coachService;
    @Resource
    ReserveService reserveService;
    @Resource
    CarReserveService carReserveService;
    @Resource
    CarService carService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            account = adminService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            account = userService.login(account);
        } else if (RoleEnum.COACH.name().equals(account.getRole())) {
            account = coachService.login(account);
        }
        return Result.success(account);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        } else{
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        } else if (RoleEnum.COACH.name().equals(account.getRole())) {
            coachService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/count")
    public Result count() {
        int courseReserveCount = reserveService.selectAll(null).size();
        int carReserveCount = carReserveService.selectAll(null).size();
        int carCount = carService.selectAll(null).size();
        int userCount = userService.selectAll(null).size();
        Dict dict = Dict.create().set("courseReserveCount", courseReserveCount)
                .set("carReserveCount", carReserveCount)
                .set("carCount", carCount)
                .set("userCount", userCount);
        return Result.success(dict);
    }

    // 统计课程预约的数据
    @GetMapping("/pieData")
    public Result pieData() {
        List<Reserve> reserveList = reserveService.selectAll(null);
        Set<String> courseNames = reserveList.stream().map(Reserve::getCourseName).collect(Collectors.toSet());
        List<Dict> list = new ArrayList<>();
        for (String courseName : courseNames) {
            Dict dict = Dict.create().set("name", courseName).set("value", reserveList.stream().filter(reserve -> reserve.getCourseName().equals(courseName)).count());
            list.add(dict);
        }
        return Result.success(list);
    }

    // 统计车辆预约的数据
    @GetMapping("/barData")
    public Result barData() {
        List<CarReserve> reserveList = carReserveService.selectAll(null);
        Set<String> carNames = reserveList.stream().map(CarReserve::getCarName).collect(Collectors.toSet());
        List<Dict> list = new ArrayList<>();
        for (String carName : carNames) {
            Dict dict = Dict.create().set("name", carName).set("value", reserveList.stream().filter(reserve -> reserve.getCarName().equals(carName)).count());
            list.add(dict);
        }
        return Result.success(list);
    }

}

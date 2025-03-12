package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Car;
import com.example.entity.CarReserve;
import com.example.entity.Message;
import com.example.mapper.CarReserveMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆预约业务处理
 **/
@Service
public class CarReserveService {

    @Resource
    private CarReserveMapper carReserveMapper;
    @Resource
    CarService carService;
    @Resource
    MessageService messageService;

    /**
     * 新增
     */
    public void add(CarReserve carReserve) {
        Account currentUser = TokenUtils.getCurrentUser();
        carReserve.setUserId(currentUser.getId());
        carReserve.setTime(DateUtil.now());
        carReserve.setStatus("待审核");
        carReserveMapper.insert(carReserve);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        carReserveMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            carReserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    @Transactional
    public void updateById(CarReserve carReserve) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            if ("通过".equals(carReserve.getStatus())) {
                carReserve.setReturnStatus("未归还");
                Car car = carService.selectById(carReserve.getCarId());
                car.setStatus("使用中");
                carService.updateById(car);
            }
            // 发送通知消息
            String content = "您预约的车辆审核 <strong>" + carReserve.getStatus() + "</strong>，请<a href='/carReserve'>点击</a>查看";
            Message message = new Message();
            message.setIsread("否");
            message.setContent(content);
            message.setUserId(carReserve.getUserId());
            messageService.add(message);
        }

        if (RoleEnum.USER.name().equals(currentUser.getRole()) && "已归还".equals(carReserve.getReturnStatus())) {
            Car car = carService.selectById(carReserve.getCarId());
            car.setStatus("可用");
            carService.updateById(car);
        }
        carReserveMapper.updateById(carReserve);
    }

    /**
     * 根据ID查询
     */
    public CarReserve selectById(Integer id) {
        return carReserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<CarReserve> selectAll(CarReserve carReserve) {
        return carReserveMapper.selectAll(carReserve);
    }

    /**
     * 分页查询
     */
    public PageInfo<CarReserve> selectPage(CarReserve carReserve, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            carReserve.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<CarReserve> list = carReserveMapper.selectAll(carReserve);
        return PageInfo.of(list);
    }

}
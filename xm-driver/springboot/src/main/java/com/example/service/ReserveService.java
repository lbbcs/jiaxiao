package com.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Course;
import com.example.entity.Message;
import com.example.entity.Reserve;
import com.example.exception.CustomException;
import com.example.mapper.ReserveMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程预约业务处理
 **/
@Service
public class ReserveService {

    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    CourseService courseService;
    @Resource
    MessageService messageService;

    /**
     * 新增
     */
    public void add(Reserve reserve) {
        Course course = courseService.selectById(reserve.getCourseId());
        Integer max = course.getMax();  // 最大人数
        int count = reserveMapper.selectByCourseId(reserve.getCourseId());  // 当前的预约通过的人数
        if (count >= max) {  // 已约满
            throw new CustomException(ResultCodeEnum.RESERVE_ERROR);
        }

        Account currentUser = TokenUtils.getCurrentUser();
        reserve.setUserId(currentUser.getId());
        reserve.setTime(DateUtil.now());
        reserve.setStatus("待审核");
        reserveMapper.insert(reserve);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        reserveMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    @Transactional
    public void updateById(Reserve reserve) {
        reserveMapper.updateById(reserve);
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            Course course = courseService.selectById(reserve.getCourseId());
            Integer max = course.getMax();  // 最大人数
            int count = reserveMapper.selectByCourseId(reserve.getCourseId());  // 当前的预约通过的人数
            if (count == max) {  // 已约满
                // 更新课程状态
                course.setStatus("已约满");
                courseService.updateById(course);
            }

            // 发送通知消息
            String content = "您预约的课程审核 <strong>" + reserve.getStatus() + "</strong>，请<a href='/reserve'>点击</a>查看";
            Message message = new Message();
            message.setIsread("否");
            message.setContent(content);
            message.setUserId(reserve.getUserId());
            messageService.add(message);
        }
    }

    /**
     * 根据ID查询
     */
    public Reserve selectById(Integer id) {
        return reserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reserve> selectAll(Reserve reserve) {
        return reserveMapper.selectAll(reserve);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reserve> selectPage(Reserve reserve, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            reserve.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> list = reserveMapper.selectAll(reserve);
        return PageInfo.of(list);
    }

    public Reserve selectByCourseIdAndUserId(Integer courseId, Integer userId) {
        return reserveMapper.selectByCourseIdAndUserId(courseId, userId);
    }

    public List<Reserve> selectUser() {
        Account currentUser = TokenUtils.getCurrentUser();
        List<Reserve> reserveList = CollUtil.newArrayList();
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {
            // 查询用户预约审核通过的所有课程信息
            List<Reserve> list = reserveMapper.selectUser(currentUser.getId());
            for (Reserve reserve : list) {
                Integer courseId = reserve.getCourseId();
                Course course = courseService.selectById(courseId);
                Integer during = course.getDuring();  // 课程的上课时长
                String start = reserve.getStart();
                DateTime startTime = DateUtil.parseDate(start);  // 把字符串的日期转换成吧标准日期格式
                for (int i = 0; i < during; i++) {
                    DateTime dateTime = DateUtil.offsetDay(startTime, i);
                    String date = DateUtil.formatDate(dateTime);
                    Reserve res = new Reserve();
                    BeanUtil.copyProperties(reserve, res);  // 属性拷贝
                    res.setStart(date);  // start 就是日历的每一天的数据
                    reserveList.add(res);
                }
            }
        }

        return reserveList;
    }

}
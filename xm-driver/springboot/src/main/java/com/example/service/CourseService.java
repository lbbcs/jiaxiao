package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Course;
import com.example.entity.Reserve;
import com.example.mapper.CourseMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 课程信息业务处理
 **/
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;
    @Resource
    ReserveService reserveService;

    /**
     * 新增
     */
    public void add(Course course) {
        course.setStatus("可用");
        if (ObjectUtil.isEmpty(course.getListing())) {
            course.setListing("下架");
        }
        courseMapper.insert(course);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            courseMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    /**
     * 根据ID查询
     */
    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Course> selectAll(Course course) {
        return courseMapper.selectAll(course);
    }

    /**
     * 分页查询
     */
    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll(course);
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {
            for (Course c : list) {
                Reserve reserve = reserveService.selectByCourseIdAndUserId(c.getId(), currentUser.getId());
                c.setUserReserve(reserve != null);
            }
        }
        return PageInfo.of(list);
    }

}
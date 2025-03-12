package com.example.mapper;

import com.example.entity.Reserve;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作reserve相关数据接口
 */
public interface ReserveMapper {

    /**
     * 新增
     */
    int insert(Reserve reserve);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Reserve reserve);

    /**
     * 根据ID查询
     */
    Reserve selectById(Integer id);

    /**
     * 查询所有
     */
    List<Reserve> selectAll(Reserve reserve);

    @Select("select * from reserve where course_id = #{courseId} and user_id = #{userId} and (status = '通过' or status = '待审核')")
    Reserve selectByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    List<Reserve> selectUser(Integer userId);

    @Select("select count(id) from reserve where course_id = #{courseId} and status = '通过'")
    int selectByCourseId(Integer courseId);

}
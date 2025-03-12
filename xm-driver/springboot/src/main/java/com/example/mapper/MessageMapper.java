package com.example.mapper;

import com.example.entity.Message;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作message相关数据接口
 */
public interface MessageMapper {

    /**
     * 新增
     */
    int insert(Message message);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Message message);

    /**
     * 根据ID查询
     */
    Message selectById(Integer id);

    /**
     * 查询所有
     */
    List<Message> selectAll(Message message);

    @Update("update message set isread = '是' where user_id = #{userId}")
    void updateRead(Integer userId);

}
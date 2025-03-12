package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 消息通知业务处理
 **/
@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    /**
     * 新增
     */
    public void add(Message message) {
        messageMapper.insert(message);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        messageMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            messageMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Message message) {
        messageMapper.updateById(message);
    }

    /**
     * 根据ID查询
     */
    public Message selectById(Integer id) {
        return messageMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Message> selectAll(Message message) {
        return messageMapper.selectAll(message);
    }

    /**
     * 分页查询
     */
    public PageInfo<Message> selectPage(Message message, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            message.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Message> list = messageMapper.selectAll(message);
        return PageInfo.of(list);
    }

    public void updateRead() {
        // 更新当前用户的所有消息为已读
        Account currentUser = TokenUtils.getCurrentUser();
        messageMapper.updateRead(currentUser.getId());
    }

}
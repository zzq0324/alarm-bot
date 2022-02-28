package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.dao.MessageDao;
import cn.zzq0324.alarm.bot.core.entity.Message;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: MessageService <br>
 * date: 2022/2/22 3:35 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    public void add(Message message) {
        messageDao.insert(message);
    }

    public Page<Message> listPage(int currentPage, int size, String chatGroupId) {
        Page page = new Page(currentPage, size);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chat_group_id", chatGroupId);

        queryWrapper.orderByAsc("send_time");

        return messageDao.selectPage(page, queryWrapper);
    }
}

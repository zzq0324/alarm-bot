package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.dao.MessageDao;
import cn.zzq0324.alarm.bot.entity.Message;
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
}

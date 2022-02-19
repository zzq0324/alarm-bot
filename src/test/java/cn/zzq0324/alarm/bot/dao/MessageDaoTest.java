package cn.zzq0324.alarm.bot.dao;

import cn.zzq0324.alarm.bot.constant.MessageType;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.vo.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

/**
 * description: MessageDaoTest <br>
 * date: 2022/2/12 3:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class MessageDaoTest {

    @Autowired
    private MessageDao messageDao;

    @Test
    public void testInsert() {
        Message message = new Message();
        message.setThirdMessageId("thirdMessageId");
        message.setContent(UUID.randomUUID().toString());
        message.setSender(new Operator("testName", "openId", "unionId"));
        message.setChatGroupId("chatGroupId");
        message.setSendTime(new Date());
        message.setMessageType(MessageType.TEXT);

        messageDao.insert(message);
        Assertions.assertTrue(message.getId() > 0);
    }
}

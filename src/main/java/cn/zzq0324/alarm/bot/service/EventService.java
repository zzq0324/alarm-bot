package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.constant.Status;
import cn.zzq0324.alarm.bot.dao.EventDao;
import cn.zzq0324.alarm.bot.entity.Event;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * description: EventService <br>
 * date: 2022/2/19 11:58 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class EventService {

    @Autowired
    private EventDao eventDao;

    public void addEvent(Event event) {
        eventDao.insert(event);
    }

    public Event getByThirdMessageId(String thirdMessageId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("third_message_id", thirdMessageId);

        return eventDao.selectOne(queryWrapper);
    }

    public Event getByChatGroupId(String chatGroupId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("chat_group_id", chatGroupId);

        return eventDao.selectOne(queryWrapper);
    }

    public void closeEvent(Event event) {
        // 设置完成时间
        event.setFinishTime(new Date());
        // 设置状态
        event.setEventStatus(Status.FINISH);
        eventDao.updateById(event);
    }
}

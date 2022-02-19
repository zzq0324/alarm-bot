package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.dao.EventDao;
import cn.zzq0324.alarm.bot.entity.Event;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Test
    public void addEvent(Event event) {
        eventDao.insert(event);
    }

    public Event getByThirdMessageId(String thirdMessageId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("third_message_id", thirdMessageId);

        return eventDao.selectOne(queryWrapper);
    }
}

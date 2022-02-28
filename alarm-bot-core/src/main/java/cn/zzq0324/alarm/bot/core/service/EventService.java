package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.constant.Status;
import cn.zzq0324.alarm.bot.core.dao.EventDao;
import cn.zzq0324.alarm.bot.core.entity.Event;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public Page<Event> listPage(int currentPage, int size, long projectId, int status, Date startDate, Date endDate) {
        Page page = new Page(currentPage, size);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (projectId > 0) {
            queryWrapper.like("project_id", projectId);
        }

        if (status >= 0) {
            queryWrapper.eq("event_status", status);
        }

        if (startDate != null) {
            queryWrapper.gt("create_time", startDate);
        }

        if (endDate != null) {
            queryWrapper.lt("create_time", endDate);
        }

        queryWrapper.orderByDesc("id");

        return eventDao.selectPage(page, queryWrapper);
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

    public List<Event> getPendingEventList() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("event_status", Status.CREATED.getValue());

        return eventDao.selectList(queryWrapper);
    }
}

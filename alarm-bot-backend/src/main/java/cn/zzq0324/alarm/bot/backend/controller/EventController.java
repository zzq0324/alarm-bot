package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.EventRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.service.EventService;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * description: EventController <br>
 * date: 2022/2/25 10:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/list")
    public Page list(EventRequest request) {
        // 设置时间段
        Date startDate = DateUtils.formatDate(request.getStartDate(), DateUtils.YYYY_MM_DD);
        Date endDate = DateUtils.formatDate(request.getEndDate(), DateUtils.YYYY_MM_DD);
        endDate = DateUtils.add(endDate, 1, Calendar.DATE);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Event> page =
            eventService.listPage(request.getPage(), request.getRows(), request.getProjectId(), request.getStatus(),
                startDate, endDate);

        List<JSONObject> list = new ArrayList<>();
        page.getRecords().stream().forEach(data -> {
            JSONObject item = (JSONObject)JSONObject.toJSON(data);
            long projectId = item.getLong("projectId");
            Project project = projectService.getById(projectId);
            item.put("projectName", project.getName());
            item.put("createTime", data.getCreateTime().getTime());
            item.put("finishTime", data.getFinishTime() == null ? -1 : data.getFinishTime().getTime());

            list.add(item);
        });

        return new Page(page.getTotal(), list);
    }
}

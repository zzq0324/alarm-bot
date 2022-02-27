package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.ProjectRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: ProjectController <br>
 * date: 2022/2/25 10:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 获取成员分页
     */
    @GetMapping("/list")
    public Page list(ProjectRequest request) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page =
            projectService.listPage(request.getPage(), request.getRows(), request.getName(), request.getStatus(),
                request.getOwnerName());

        return new Page(page);
    }
}

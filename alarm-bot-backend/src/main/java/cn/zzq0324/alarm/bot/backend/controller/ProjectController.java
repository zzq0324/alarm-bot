package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.ProjectRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.service.MemberService;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: ProjectController <br>
 * date: 2022/2/25 10:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Map<Long, String> MEMBER_NAME_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MemberService memberService;

    /**
     * 获取成员分页
     */
    @GetMapping("/list")
    public Page list(ProjectRequest request) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page =
            projectService.listPage(request.getPage(), request.getRows(), request.getName(), request.getStatus(),
                request.getOwnerName());

        List<JSONObject> list = new ArrayList<>();
        // 转化memberName方便前端展示
        page.getRecords().stream().forEach(data -> {
            JSONObject item = (JSONObject)JSONObject.toJSON(data);
            String memberIds = item.getString("memberIds");
            item.put("memberNames", getMemberName(memberIds));

            list.add(item);
        });

        return new Page(page.getTotal(), list);
    }

    private String getMemberName(String memberIds) {
        StringBuilder memberNameBuilder = new StringBuilder();
        if (StringUtils.hasLength(memberIds)) {
            String[] memberIdArr = StringUtils.commaDelimitedListToStringArray(memberIds);
            for (String memberId : memberIdArr) {
                String memberName = MEMBER_NAME_CACHE.computeIfAbsent(Long.parseLong(memberId), id -> {
                    Member member = memberService.get(Long.parseLong(memberId));
                    return member.getName();
                });

                if (memberNameBuilder.length() > 0) {
                    memberNameBuilder.append("，");
                }
                memberNameBuilder.append(memberName);
            }
        }

        return memberNameBuilder.toString();
    }

    @RequestMapping("/add")
    public String add(Project project) {
        // 检测项目是否存在
        Project projectInDB = projectService.getByName(project.getName());
        if (projectInDB != null) {
            return "duplicate";
        }

        setOwnerName(project);
        projectService.add(project);

        return "success";
    }

    @RequestMapping("/update")
    public void update(Project project) {
        setOwnerName(project);
        projectService.update(project);
    }

    private void setOwnerName(Project project) {
        Member owner = memberService.get(project.getOwnerId());
        project.setOwnerName(owner.getName());
    }
}

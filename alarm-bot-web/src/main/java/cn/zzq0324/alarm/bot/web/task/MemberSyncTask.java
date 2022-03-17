package cn.zzq0324.alarm.bot.web.task;

import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.service.MemberService;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.vo.MemberThirdAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * description: 成员同步定时任务 <br>
 * date: 2022/2/22 1:43 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
@Slf4j
public class MemberSyncTask {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ProjectService projectService;

    @Scheduled(cron = "${alarm-bot.leave-member-notify-cron}")
    public void execute() {
        List<Member> memberList = memberService.getNormalMemberList();

        try {
            // 检测人员的在职状态，离职直接更新状态
            memberList.stream().forEach(member -> checkAndMark(member));
        } catch (Exception e) {
            log.error("sync member error", e);
        }

        checkProjectOwnerAndMember();
    }

    /**
     * 校验项目的owner和member
     */
    private void checkProjectOwnerAndMember() {
        // 正常状态的成员列表
        List<Member> normalMemberList = memberService.getNormalMemberList();
        // Key为memberId，value为Member对象
        Map<Long, Member> normalMemberMap =
            normalMemberList.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

        // 正常状态的项目列表
        List<Project> projectList = projectService.getNormalProjectList();

        // key为memberId，value为对应负责的项目集合
        Map<Long, Set<Project>> leaveMemberIdProjectMap = new HashMap<>();
        for (Project project : projectList) {
            // 已下线，不处理
            if (project.getStatus() == Project.STATUS_OFFLINE) {
                continue;
            }

            // 判断项目owner是否有离职
            checkMemberLeave(normalMemberMap, leaveMemberIdProjectMap, project, project.getOwnerId());

            // 判断项目member是否有离职
            if (StringUtils.hasLength(project.getMemberIds())) {
                Set<String> memberIdSet = StringUtils.commaDelimitedListToSet(project.getMemberIds());
                for (String memberIdStr : memberIdSet) {
                    checkMemberLeave(normalMemberMap, leaveMemberIdProjectMap, project, Long.parseLong(memberIdStr));
                }
            }
        }

        if (CollectionUtils.isEmpty(leaveMemberIdProjectMap)) {
            return;
        }

        // 如果有人员离职，推送消息
        Map<Member, Set<Project>> leaveMemberProjectMap = new HashMap<>();
        for (Long memberId : leaveMemberIdProjectMap.keySet()) {
            Member member = memberService.get(memberId);
            leaveMemberProjectMap.put(member, leaveMemberIdProjectMap.get(memberId));
        }

        ExtensionLoader.getDefaultExtension(PlatformExt.class).memberLeaveNotify(leaveMemberProjectMap);
    }

    /**
     * 校验离职成员
     */
    private void checkMemberLeave(Map<Long, Member> normalMemberMap, Map<Long, Set<Project>> leaveMemberIdProjectMap,
        Project project, Long memberId) {
        // 兼容owner为空的数据
        if (memberId == null) {
            return;
        }

        // 在职不判断
        if (normalMemberMap.containsKey(memberId)) {
            return;
        }

        // 不在职添加到离职列表，并添加负责的新项目到集合
        leaveMemberIdProjectMap.putIfAbsent(memberId, new HashSet<>());
        leaveMemberIdProjectMap.get(memberId).add(project);
    }

    /**
     * 校验并标记人员离职状态
     */
    private void checkAndMark(Member member) {
        try {
            // 查询三方信息
            MemberThirdAuthInfo memberThirdAuthInfo =
                ExtensionLoader.getDefaultExtension(PlatformExt.class).getMemberInfo(member.getIdentity());

            // 如果能查到代表还在职，不处理
            if (memberThirdAuthInfo != null) {
                return;
            }

            // 查不到，标记离职
            member.setStatus(Member.STATUS_DISABLE);
            memberService.update(member);
        } catch (Exception e) {
            log.error("check member {} status error", member.getName(), e);
        }
    }
}

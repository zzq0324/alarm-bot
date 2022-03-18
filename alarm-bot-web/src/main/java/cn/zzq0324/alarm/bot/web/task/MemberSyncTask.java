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
import java.util.Iterator;
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
        Map<Long, Set<Project>> leaveOwnerIdProjectMap = new HashMap<>();
        for (Project project : projectList) {
            // 已下线，不处理
            if (project.getStatus() == Project.STATUS_OFFLINE) {
                continue;
            }

            // 判断项目owner是否有离职，离职添加到离职Owner列表
            if (project.getOwnerId() != null && isLeave(normalMemberMap, project.getOwnerId())) {
                leaveOwnerIdProjectMap.putIfAbsent(project.getOwnerId(), new HashSet<>());
                leaveOwnerIdProjectMap.get(project.getOwnerId()).add(project);
            }

            deleteLeaveMember(normalMemberMap, project);
        }

        if (CollectionUtils.isEmpty(leaveOwnerIdProjectMap)) {
            return;
        }

        // 如果有人员离职，推送消息
        Map<Member, Set<Project>> leaveMemberProjectMap = new HashMap<>();
        for (Long memberId : leaveOwnerIdProjectMap.keySet()) {
            Member member = memberService.get(memberId);
            leaveMemberProjectMap.put(member, leaveOwnerIdProjectMap.get(memberId));
        }

        ExtensionLoader.getDefaultExtension(PlatformExt.class).memberLeaveNotify(leaveMemberProjectMap);
    }

    /**
     * Member直接移除
     */
    private void deleteLeaveMember(Map<Long, Member> normalMemberMap, Project project) {
        if (!StringUtils.hasLength(project.getMemberIds())) {
            return;
        }

        Set<String> memberIdSet = StringUtils.commaDelimitedListToSet(project.getMemberIds());
        boolean hasLeave = false;
        for (Iterator<String> it = memberIdSet.iterator(); it.hasNext(); ) {
            String memberIdStr = it.next();
            Long memberId = Long.parseLong(memberIdStr);
            if (isLeave(normalMemberMap, memberId)) {
                hasLeave = true;
                it.remove();
            }
        }

        if (hasLeave) {
            project.setMemberIds(StringUtils.collectionToCommaDelimitedString(memberIdSet));
            projectService.update(project);
        }
    }

    private boolean isLeave(Map<Long, Member> normalMemberMap, Long memberId) {
        return !normalMemberMap.containsKey(memberId);
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

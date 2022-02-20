package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.constant.EventStatus;
import cn.zzq0324.alarm.bot.entity.Event;
import cn.zzq0324.alarm.bot.entity.Member;
import cn.zzq0324.alarm.bot.entity.MemberPlatformInfo;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Project;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.CreateEventContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.EventService;
import cn.zzq0324.alarm.bot.service.MemberService;
import cn.zzq0324.alarm.bot.service.ProjectService;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: CreateEvent <br>
 * date: 2022/2/19 11:36 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.CREATE_EVENT, summary = "指令-创建事件")
@Slf4j
public class CreateEvent implements Command<CreateEventContext> {

    private static String CHAT_GROUP_NAME = "⚠️[%s]告警群";
    private static String CHAT_GROUP_DESC = "问题结束后请按格式标记解决，有任何问题可以先@Bot";

    @Autowired
    private AlarmBotProperties alarmBotProperties;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EventService eventService;

    private Pattern pattern = null;

    @PostConstruct
    public void initPattern() {
        pattern = Pattern.compile(alarmBotProperties.getProjectNameRegExp());
    }

    @Override
    public CommandContext matchCommand(Message message) {
        // 根据正则表达式查找project
        String projectName = extractProjectName(message.getContent());
        Project project = StringUtils.isEmpty(projectName) ? null : projectService.getByName(projectName);

        // 符合新建事件，根据正则表达式能得出projectName并且数据库有配置该项目的告警监听
        if (StringUtils.hasLength(projectName) && project != null) {
            return CreateEventContext.builder().project(project).message(message).command(CommandConstants.CREATE_EVENT)
                .build();
        }

        return null;
    }

    @Override
    public void execute(CreateEventContext context) {
        // 根据项目名称查找对应的成员
        Project project = context.getProject();
        if (StringUtils.isEmpty(project.getMemberIds())) {
            log.warn("project: {} memberIds not configured!", project.getName());

            return;
        }

        Message message = context.getMessage();
        // 查询事件是否已经创建过
        Event existEvent = eventService.getByThirdMessageId(message.getThirdMessageId());
        if (existEvent != null) {
            log.warn("event: {} exists", JSONObject.toJSONString(existEvent));

            return;
        }

        // 查找项目对应的人，用于接收告警信息
        List<String> thirdPlatformOpenIdList = getMemberOpenIdList(project);

        // 创建群聊并拉人入群
        String chatGroupId = createChatGroup(project, thirdPlatformOpenIdList);

        // 插入事件记录
        Event event = addEvent(project, message, chatGroupId);

        // 推送故障或问题
        ExtensionLoader.getDefaultExtension(PlatformExt.class).pushEvent(event, project);

        //  回复群消息，告知拉群处理
        ExtensionLoader.getDefaultExtension(PlatformExt.class)
            .replyText(message.getThirdMessageId(), alarmBotProperties.getReplyAlarm());
    }

    private String createChatGroup(Project project, List<String> thirdPlatformOpenIdList) {
        String chatGroupName = String.format(CHAT_GROUP_NAME, project.getName());
        // 找到直接创建群聊，拉人并发送消息
        String chatGroupId =
            ExtensionLoader.getDefaultExtension(PlatformExt.class).createChatGroup(chatGroupName, CHAT_GROUP_DESC);
        // 拉人进群
        ExtensionLoader.getDefaultExtension(PlatformExt.class)
            .addMemberToChatGroup(chatGroupId, thirdPlatformOpenIdList);

        return chatGroupId;
    }

    private Event addEvent(Project project, Message message, String chatGroupId) {
        Event event = new Event();
        event.setEventStatus(EventStatus.CREATED);
        event.setChatGroupId(chatGroupId);
        event.setDetail(message.getContent());
        event.setThirdMessageId(message.getThirdMessageId());
        event.setProjectId(project.getId());
        event.setCreateTime(new Date());

        eventService.addEvent(event);

        return event;
    }

    private List<String> getMemberOpenIdList(Project project) {
        // 查询对应的人
        List<String> thirdPlatformOpenIdList = new ArrayList<>();
        Set<String> memberIdSet = StringUtils.commaDelimitedListToSet(project.getMemberIds());
        for (String memberIdStr : memberIdSet) {
            Member member = memberService.get(Long.parseLong(memberIdStr));
            MemberPlatformInfo memberPlatformInfo = memberService.getMemberPlatformInfo(member, true);

            thirdPlatformOpenIdList.add(memberPlatformInfo.getOpenId());
        }

        return thirdPlatformOpenIdList;
    }

    /**
     * 从消息体中解析出serviceId
     */
    private String extractProjectName(String messageContent) {
        Matcher matcher = pattern.matcher(messageContent);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}

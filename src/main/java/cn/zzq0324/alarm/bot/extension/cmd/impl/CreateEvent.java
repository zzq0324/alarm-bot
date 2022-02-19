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

    private static String CHAT_GROUP_NAME = "[%s]告警群";

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
        log.info("project name reg expression: {}", alarmBotProperties.getProjectNameRegExp());
        pattern = Pattern.compile(alarmBotProperties.getProjectNameRegExp());
    }

    @Override
    public CommandContext matchCommand(Message message) {
        log.info("message: {}", JSONObject.toJSONString(message));
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

        // 查询事件是否已经创建过
        Event existEvent = eventService.getByThirdMessageId(context.getMessage().getThirdMessageId());
        if (existEvent != null) {
            log.warn("event: {} exists", JSONObject.toJSONString(existEvent));

            return;
        }

        String chatGroupName = String.format(CHAT_GROUP_NAME, project.getName());

        // 查询对应的人
        List<String> thirdPlatformOpenIdSet = new ArrayList<>();
        Set<String> memberIdSet = StringUtils.commaDelimitedListToSet(project.getMemberIds());
        for (String memberIdStr : memberIdSet) {
            Member member = memberService.get(Long.parseLong(memberIdStr));
            MemberPlatformInfo memberPlatformInfo = memberService.getMemberPlatformInfo(member, true);

            thirdPlatformOpenIdSet.add(memberPlatformInfo.getOpenId());
        }

        // 找到直接创建群聊，拉人并发送消息
        String chatGroupId = ExtensionLoader.getDefaultExtension(PlatformExt.class).createChatGroup(chatGroupName, "");

        // 拉人进群
        ExtensionLoader.getDefaultExtension(PlatformExt.class)
            .addMemberToChatGroup(chatGroupId, thirdPlatformOpenIdSet);

        // 推送故障或问题
        // TODO

        // 插入事件记录
        Event event = new Event();
        event.setEventStatus(EventStatus.CREATED);
        event.setChatGroupId(context.getMessage().getChatGroupId());
        event.setDetail(context.getMessage().getContent());
        event.setThirdMessageId(context.getMessage().getThirdMessageId());
        event.setProjectId(project.getId());

        eventService.addEvent(event);
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

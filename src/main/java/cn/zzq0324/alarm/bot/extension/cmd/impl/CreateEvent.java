package cn.zzq0324.alarm.bot.extension.cmd.impl;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Project;
import cn.zzq0324.alarm.bot.extension.cmd.Command;
import cn.zzq0324.alarm.bot.extension.cmd.context.CreateEventContext;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.service.ProjectService;
import cn.zzq0324.alarm.bot.spi.Extension;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: CreateEvent <br>
 * date: 2022/2/19 11:36 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = CommandConstants.CREATE_EVENT, summary = "指令-创建事件")
public class CreateEvent implements Command<CreateEventContext> {

    @Autowired
    private AlarmBotProperties alarmBotProperties;
    @Autowired
    private ProjectService projectService;

    private Pattern pattern = null;

    @PostConstruct
    public void initPattern() {
        pattern = Pattern.compile(alarmBotProperties.getProjectNameRegExp());
    }

    @Override
    public void execute(CreateEventContext context) {
        Message message = context.getMessage();
        // 根据正则表达式查找project
        String projectName = extractProjectName(message.getContent());
        Project project = StringUtils.isEmpty(projectName) ? null : projectService.getByName(projectName);

        // 找不到项目回复消息提示配置有误
        if (StringUtils.isEmpty(projectName) || project == null) {
            projectNotFoundTip(message.getThirdMessageId());

            return;
        }

        // 创建事件
        handleCreateEvent(project, message);
    }

    private void handleCreateEvent(Project project, Message message) {
        // 根据项目名称查找对应的成员

        // 找到直接创建群聊，拉人并发送消息
        String chatGroupId = ExtensionLoader.getDefaultExtension(PlatformExt.class).createChatGroup("", "");

        // 查询对应的人

        // 拉人进群
        ExtensionLoader.getDefaultExtension(PlatformExt.class).addMemberToChatGroup(chatGroupId, null);

        // 推送故障或问题

        // 插入数据库记录
    }

    /**
     * 未找到服务ID，给出对应的提示
     */
    private void projectNotFoundTip(String thirdMessageId) {
        String text = "根据配置值[" + alarmBotProperties.getProjectNameRegExp() + "]无法解析出项目名称，请检查配置或内容是否正确！";

        // 群聊发起，推送正则表达式错误的提示到群聊
        if (!StringUtils.isEmpty(thirdMessageId)) {
            ExtensionLoader.getDefaultExtension(PlatformExt.class).reply(thirdMessageId, "【温馨提醒】", text);
        } else {
            throw new IllegalArgumentException(text);
        }
    }

    /**
     * 从消息体中解析出serviceId
     */
    private String extractProjectName(String messageContent) {
        if (pattern == null) {
            pattern = Pattern.compile(alarmBotProperties.getProjectNameRegExp());
        }

        Matcher matcher = pattern.matcher(messageContent);
        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }
}

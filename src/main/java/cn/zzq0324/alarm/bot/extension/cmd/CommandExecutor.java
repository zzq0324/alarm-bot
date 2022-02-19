package cn.zzq0324.alarm.bot.extension.cmd;

import cn.zzq0324.alarm.bot.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.constant.CommandConstants;
import cn.zzq0324.alarm.bot.entity.Message;
import cn.zzq0324.alarm.bot.entity.Project;
import cn.zzq0324.alarm.bot.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.CreateEventContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.HelpContext;
import cn.zzq0324.alarm.bot.extension.cmd.context.SolveEventContext;
import cn.zzq0324.alarm.bot.service.ProjectService;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 命令执行器 <br>
 * date: 2022/2/19 3:23 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Component
public class CommandExecutor {

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Autowired
    private ProjectService projectService;

    private Pattern pattern = null;

    @PostConstruct
    public void initPattern() {
        pattern = Pattern.compile(alarmBotProperties.getProjectNameRegExp());
    }

    public void execute(Message message) {
        // 根据message解析并返回上下文
        CommandContext context = parseContext(message);

        // 执行命令
        ExtensionLoader.getExtension(Command.class, context.getCommand()).execute(context);
    }

    private CommandContext parseContext(Message message) {
        // 根据正则表达式查找project
        String projectName = extractProjectName(message.getContent());
        Project project = StringUtils.isEmpty(projectName) ? null : projectService.getByName(projectName);

        // 符合新建事件，根据正则表达式能得出projectName并且数据库有配置该项目的告警监听
        if (StringUtils.hasLength(projectName) && project != null) {
            return CreateEventContext.builder().project(project).message(message).command(CommandConstants.CREATE_EVENT)
                .build();
        }

        // 解决事件
        if (message.getContent().contains(CommandConstants.SOLVE_EVENT)) {
            return SolveEventContext.builder().command(CommandConstants.SOLVE_EVENT).build();
        }

        // 帮助命令，兜底
        return HelpContext.builder().command(CommandConstants.HELP).message(message).build();
    }

    /**
     * 从消息体中解析出serviceId
     */
    private String extractProjectName(String messageContent) {
        if (StringUtils.isEmpty(messageContent)) {
            return null;
        }
        
        Matcher matcher = pattern.matcher(messageContent);
        if (matcher.find()) {
            return matcher.group(0).trim();
        }

        return null;
    }
}

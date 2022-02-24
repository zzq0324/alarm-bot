package cn.zzq0324.alarm.bot.core.extension.cmd;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.CommandConstants;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.CommandContext;
import cn.zzq0324.alarm.bot.core.extension.cmd.context.HelpContext;
import cn.zzq0324.alarm.bot.core.extension.cmd.impl.Help;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
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
        List<Command> commandList = ExtensionLoader.getExtensionLoader(Command.class).getExtensionList();
        CommandContext context = null;

        for (Command command : commandList) {
            // 帮助命令优先级最低
            if (command instanceof Help) {
                continue;
            }

            context = command.matchCommand(message);

            // 匹配上命令，不继续匹配
            if (context != null) {
                break;
            }
        }

        // 匹配不上，执行帮助
        if (context == null) {
            context = HelpContext.builder().command(CommandConstants.HELP).message(message).build();
        }

        ExtensionLoader.getExtension(Command.class, context.getCommand()).execute(context);
    }
}

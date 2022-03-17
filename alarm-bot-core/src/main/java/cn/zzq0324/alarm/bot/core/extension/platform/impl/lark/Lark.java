package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.LarkConstants;
import cn.zzq0324.alarm.bot.core.constant.PlatformType;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import cn.zzq0324.alarm.bot.core.util.FileUtils;
import cn.zzq0324.alarm.bot.core.vo.CallbackData;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import cn.zzq0324.alarm.bot.core.vo.MemberThirdAuthInfo;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.request.RequestOptFn;
import com.larksuite.oapi.core.utils.Jsons;
import com.larksuite.oapi.service.contact.v3.model.User;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * description: Lark <br>
 * date: 2022/2/18 9:57 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Extension(name = PlatformType.LARK, isDefault = true, summary = "飞书")
@Slf4j
public class Lark implements PlatformExt {

    private static final RequestOptFn TIMEOUT_OPT = Request.setTimeout(1, TimeUnit.MINUTES);

    @Autowired
    private LarkHelper larkHelper;

    @Autowired
    private AlarmBotProperties alarmBotProperties;

    @Autowired
    private ProjectService projectService;

    @Override
    public void replyText(String messageId, String text) {
        larkHelper.replyText(messageId, null, text);
    }

    @Override
    public void pushEvent(Event event, Project project) {
        JSONObject data = (JSONObject)JSONObject.toJSON(event);
        data.put("projectName", project.getName());
        String cardContent = FileUtils.getFileContent("/lark/event-info.json", data, true);

        larkHelper.send(event.getChatGroupId(), LarkConstants.MESSAGE_TYPE_INTERACTIVE, cardContent);
    }

    @Override
    public void pendingTaskNotify(Event event) {
        JSONObject data = (JSONObject)JSONObject.toJSON(event);
        String durationText = DateUtils.getDiffText(event.getCreateTime(), new Date());
        data.put("durationText", durationText);

        Project project = projectService.getById(event.getProjectId());
        data.put("projectName", project.getName());

        String cardContent = FileUtils.getFileContent("/lark/pending-event-notify.json", data, true);

        larkHelper.send(event.getChatGroupId(), LarkConstants.MESSAGE_TYPE_INTERACTIVE, cardContent);
    }

    @Override
    public IMMessage parseIMMessage(CallbackData callbackData) {
        // 回调数据分sender和message
        MessageReceiveEventData eventData =
            Jsons.DEFAULT_GSON.fromJson(callbackData.getData().toJSONString(), MessageReceiveEventData.class);

        return larkHelper.parse(eventData);
    }

    @Override
    public String createChatGroup(String name, String description) {
        return larkHelper.createChatGroup(name, description);
    }

    @Override
    public void addMemberToChatGroup(String chatGroupId, List<String> memberIdList) {
        larkHelper.addMemberToChatGroup(chatGroupId, memberIdList);
    }

    @Override
    public void destroyChatGroup(String chatGroupId) {
        larkHelper.destroyChatGroup(chatGroupId);
    }

    @Override
    public void help(Message message) {
        JSONObject data = (JSONObject)JSONObject.toJSON(alarmBotProperties);
        // 获取帮助模板
        String content = FileUtils.getFileContent("/lark/help.json", data, true);

        // 响应式卡片
        larkHelper.send(message.getChatGroupId(), LarkConstants.MESSAGE_TYPE_INTERACTIVE, content);
    }

    @Override
    public MemberThirdAuthInfo getMemberInfo(String identify) {
        String openId = larkHelper.getOpenIdByIdentify(identify);
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        // 查询人员信息
        User user = larkHelper.getLarkUser(openId, "open_id");

        return new MemberThirdAuthInfo(user.getName(), user.getOpenId(), user.getUnionId());
    }

    @Override
    public void memberLeaveNotify(Map<Member, Set<Project>> memberProjectMap) {
        StringBuilder messageBuilder = new StringBuilder("<at user_id=\"all\">所有人</at> ");
        messageBuilder.append(alarmBotProperties.getMemberLeaverNotifyText());
        for (Member member : memberProjectMap.keySet()) {
            Set<Project> projectSet = memberProjectMap.get(member);
            List<String> projectNameList =
                projectSet.stream().map(project -> project.getName()).collect(Collectors.toList());

            messageBuilder.append("\n");
            messageBuilder.append("姓名：").append(member.getName()).append("，标识：").append(member.getIdentity())
                .append("，相关项目：").append(StringUtils.collectionToDelimitedString(projectNameList, ","));
        }

        // 发送通知
        larkHelper.sendWebHookMsg(alarmBotProperties.getWebhookUrl(), null, messageBuilder.toString());
    }
}

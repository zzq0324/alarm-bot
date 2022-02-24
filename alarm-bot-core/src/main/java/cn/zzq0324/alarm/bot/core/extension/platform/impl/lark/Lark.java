package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark;

import cn.zzq0324.alarm.bot.core.config.AlarmBotProperties;
import cn.zzq0324.alarm.bot.core.constant.LarkConstants;
import cn.zzq0324.alarm.bot.core.constant.PlatformType;
import cn.zzq0324.alarm.bot.core.entity.Event;
import cn.zzq0324.alarm.bot.core.entity.MemberPlatformInfo;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.entity.Project;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.service.ProjectService;
import cn.zzq0324.alarm.bot.core.spi.Extension;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.util.DateUtils;
import cn.zzq0324.alarm.bot.core.util.FileUtils;
import cn.zzq0324.alarm.bot.core.vo.CallbackData;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser.LarkMessageParserExt;
import com.alibaba.fastjson.JSONObject;
import com.larksuite.oapi.core.api.request.Request;
import com.larksuite.oapi.core.api.request.RequestOptFn;
import com.larksuite.oapi.core.utils.Jsons;
import com.larksuite.oapi.service.contact.v3.model.User;
import com.larksuite.oapi.service.im.v1.model.EventMessage;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        EventMessage eventMessage = eventData.getMessage();

        LarkMessageParserExt messageParser =
            ExtensionLoader.getExtension(LarkMessageParserExt.class, eventMessage.getMessageType());

        // 不支持的解析，不做处理
        if (messageParser == null) {
            return null;
        }

        IMMessage imMessage = new IMMessage();
        messageParser.parse(imMessage, eventData);

        return imMessage;
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
    public MemberPlatformInfo getMemberInfo(String mobile) {
        String openId = larkHelper.getOpenIdByMobile(mobile);
        if (StringUtils.isEmpty(openId)) {
            return null;
        }

        // 查询人员信息
        User user = larkHelper.getLarkUser(openId, "open_id");

        MemberPlatformInfo memberPlatformInfo = new MemberPlatformInfo();
        memberPlatformInfo.setOpenId(user.getOpenId());
        memberPlatformInfo.setUnionId(user.getUnionId());
        memberPlatformInfo.setName(user.getName());

        return memberPlatformInfo;
    }
}

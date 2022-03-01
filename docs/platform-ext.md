> 通过扩展，可以支持飞书、钉钉、企业微信以及其他可能的IM。

## 新增实现类

新增一个实现类，需要继承`PlatformExt`，例如：

```java
@Extension(name = "dingtalk", summary = "钉钉")
public class DingTalk implements PlatformExt {

    @Override
    public void replyText(String messageId, String text) {

    }

    @Override
    public void pushEvent(Event event, Project project) {

    }

    @Override
    public void pendingTaskNotify(Event event) {
        
    }

    @Override
    public IMMessage parseIMMessage(CallbackData callbackData) {
        return null;
    }

    @Override
    public String createChatGroup(String name, String description) {
        return null;
    }

    @Override
    public void addMemberToChatGroup(String chatGroupId, List<String> memberIdList) {

    }

    @Override
    public void destroyChatGroup(String chatGroupId) {

    }

    @Override
    public void help(Message message) {

    }

    @Override
    public MemberThirdAuthInfo getMemberInfo(String mobile) {
        return null;
    }
}
```

## 增加配置信息

在`application.properties`（如果没有新增一个文件）中增加以下配置：

```
spi.cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt=dingtalk
```
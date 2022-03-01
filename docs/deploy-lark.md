## API和权限

- **调用API**
  - [创建群聊](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/chat/create)
  - [解散群](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/chat/delete)
  - [将用户或机器人拉入群聊](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/chat-members/create)
  - [获取用户或机器人所在的群列表](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/chat/list)
  - [发送消息](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/create)
  - [回复消息](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/reply)
  - [获取会话历史消息](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message/list)
  - [获取单个用户信息](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/user/get)
  - [获取消息中的资源文件](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message-resource/get)
  - [获取机器人信息](https://open.feishu.cn/document/ukTMukTMukTM/uAjMxEjLwITMx4CMyETM)
- **监听事件**
  - 接收消息
- **权限**
  - [获取群组中所有消息]，权限字符：`im:message.group_msg`
  - [获取与更新群组信息]，权限字符：`im:chat`
  - [读取群消息]，权限字符：`im:chat.group_info:readonly`
  - [更新应用所创建群的群信息]，权限字符：`im:chat:operate_as_owner`
  - [以应用身份读取通讯录]，权限字符：`contact:contact:readonly_as_app`
  - [获取用户userId]，权限字符：`contact:user.employee_id:readonly`
  - [通过手机号或邮箱获取用户ID]，权限字符：`contact:user.id:readonly`

## 应用申请

请参照官方文档[创建应用 & 申请权限](https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/im-v1/message-development-tutorial/turn-on-app-permissions)，需要开通的事件和权限信息详见上面的介绍。

回调地址参照[访问链接](/deploy-config?id=%e8%ae%bf%e9%97%ae%e9%93%be%e6%8e%a5)进行配置。


## 配置

应用申请和配置后，需要到应用的`conf`目录中配置`application.yml`中以下部分的信息：

```
lark:
  app-id: cli_xxxx
  app-secret: xxxxxxxxxxxxxx
  verify-token: xxxxxxxxxxxxxx
  encrypt-key: xxxxxxxxxxxxxx
```
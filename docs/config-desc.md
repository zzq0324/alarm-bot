## alarm-bot-web

> `alarm-bot-web`承载着三方服务的回调以及定时任务的处理（例如告警解除15分钟后定时关闭群聊等）。配置信息如下：

- **HTTP端口修改**

```
# HTTP监听端口，根据需要修改
server:
  port: 8888
```

- **定时任务配置**

```
# Spring相关配置
spring:
  # 任务线程池配置，可以保持默认即可
  task:
    scheduling:
      shutdown:
        await-termination: true
      pool:
        size: 20
        thread-name-prefix: 'alarm-bot-'
```

- **数据源配置**

数据源配置按需修改，正常修改username、password、url即可，其他的保持不变。

```
spring:
  datasource:
    username: bot
    password: Test!@#$1234
    url: jdbc:mysql://localhost:3306/alarm-bot?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    # hikari数据源特性配置
    hikari:
      # 最大连接数,默认值10.
      maximum-pool-size: 100
      # 最小空闲连接，默认值10.
      minimum-idle: 5
      # 连接超时时间(毫秒),默认值30秒.
      connection-timeout: 60000
      #空闲连接超时时间，默认值600000(10分钟),只有空闲连接数大于最大连接数且空闲时间超过该值，才会被释放
      #如果大于等于 max-lifetime 且 max-lifetime>0,则会被重置为0.
      idle-timeout: 600000
      #连接最大存活时间,默认值30分钟.设置应该比mysql设置的超时时间短
      max-lifetime: 3000000
      #连接测试查询
      connection-test-query: select 1
```

- **飞书相关的配置**

飞书相关的参数配置信息获取详见[飞书配置](/deploy-lark)

```
lark:
  app-id: cli_xxxx
  app-secret: xxxxxxxxxxxxxx
  verify-token: xxxxxxxxxxxxxx
  encrypt-key: xxxxxxxxxxxxxx
```

- **机器人信息配置**

```
# 机器人相关的配置，文本信息根据实际情况可以修改
alarm-bot:
  # 以下为必须修改属性
  # 机器人名称，和开发者后台保持一致
  bot-name: 'AlarmBot'
  # 实例，推送使用帮助的时候使用
  create-alarm-demo: '@AlarmBot [PINPOINT Alarm - service-test] HEAP USAGE  is 92%.'
  # projectName对应的正则表达式，只允许有一对括号
  project-name-reg-exp: 'PINPOINT Alarm - (\w+-\w+)'
  # 资源文件下载目录，全路径。例如下载群里的聊天图片
  resource-download-folder: '/Users/zhengzhq/Downloads/alarm-bot'
  # 地址前缀，用于拼接使用，不带/
  url-prefix: 'http://localhost:8080'

  # 以下为可选配置，看具体情况修改
  reply-alarm: '告警信息已收到，已另外创建群聊沟通，解决后会同步进度到本群'
  reply-event-solved: '感谢大家的参与，本群预计%d分钟后解散，如有重要信息请及时下载备份！'
  not-in-alarm-group: '当前群不支持 /solve 指令'
  solve-summary-missing: '告警小结缺失，请在/solve命令后带上告警小结(例如：空指针问题，已修复)'
  reply-solved-to-chat-group: '告警已解除，处理时效：%s。小结：%s'
  # N分钟后解散群聊
  destroy-group-after-minutes: 15
  # 检测最近N分钟的机器人HOOK消息
  check-robot-hook-message: 60
  # 任务每天定时提醒未跟进事项：8点至20点每两小时提醒一次
  pending-event-notify-cron: '0 0 8-20/2 * * ? '
  # 找不到项目成员
  project-member-missing: '项目未配置对应的人员，当前事件不会拉群，请及时跟进处理并做好配置工作！'
  alarm-group-reply-pending-task: '当前告警超过%s，已在告警群提醒相关同学跟进。'

```

## alarm-bot-backend

- **HTTP端口修改**

```
# HTTP监听端口，根据需要修改
server:
  port: 9999
```

- **数据源配置**

数据源配置按需修改，正常修改username、password、url即可，其他的保持不变。

```
spring:
  datasource:
    username: bot
    password: Test!@#$1234
    url: jdbc:mysql://localhost:3306/alarm-bot?characterEncoding=utf8&characterSetResults=utf8&autoReconnect=true&failOverReadOnly=false&serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    # hikari数据源特性配置
    hikari:
      # 最大连接数,默认值10.
      maximum-pool-size: 100
      # 最小空闲连接，默认值10.
      minimum-idle: 5
      # 连接超时时间(毫秒),默认值30秒.
      connection-timeout: 60000
      #空闲连接超时时间，默认值600000(10分钟),只有空闲连接数大于最大连接数且空闲时间超过该值，才会被释放
      #如果大于等于 max-lifetime 且 max-lifetime>0,则会被重置为0.
      idle-timeout: 600000
      #连接最大存活时间,默认值30分钟.设置应该比mysql设置的超时时间短
      max-lifetime: 3000000
      #连接测试查询
      connection-test-query: select 1
```

- **飞书相关的配置**

飞书相关的参数配置信息获取详见[飞书配置](/deploy-lark)

```
lark:
  app-id: cli_xxxx
  app-secret: xxxxxxxxxxxxxx
  verify-token: xxxxxxxxxxxxxx
  encrypt-key: xxxxxxxxxxxxxx
```


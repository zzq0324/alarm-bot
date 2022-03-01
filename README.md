# Alarm Bot

![Java](https://img.shields.io/badge/language-java-green.svg)
![Star](https://img.shields.io/github/stars/zzq0324/alarm-bot)
![Fork](https://img.shields.io/github/forks/zzq0324/alarm-bot)
![Issues](https://img.shields.io/github/issues/zzq0324/alarm-bot)
![Licence](https://img.shields.io/github/license/zzq0324/alarm-bot)

> Alarm Bot中文名"告警机器人"，通过三方平台的开放能力，完成告警 -> 创建群 -> 拉人进群 -> 解决告警的闭环。针对未及时处理的告警提醒相关人员及时跟进处理，确保不会遗漏。

更多信息可以查看[详细文档](https://alarm-bot.zzq0324.cn/)。

## 演示视频

https://user-images.githubusercontent.com/24889976/155530740-da21479d-4a33-46b0-8450-2b65ede2a69a.mp4

## 技术设计

### 项目结构

```
alarm-bot-parent
|
|__ alarm-bot-core（核心代码，包含SPI、Extension和dao、service类）
|
|__ alarm-bot-web（接收三方平台的事件回调以及相关定时任务）
|
|__ alarm-bot-backend（后台配置界面相关接口和前端页面）
```

### 流程图

<img src='docs/images/flow.jpg' width='600'></img>

## 未来计划

- 完善`alarm-bot-backend`，后台支持项目、成员的配置；
- 项目支持Owner和Member的角色，便于日常管理；
- 支持钉钉；
- 支持企业微信。

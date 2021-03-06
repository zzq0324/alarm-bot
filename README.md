# Alarm Bot

![Java](https://img.shields.io/badge/language-java-green.svg)
![Star](https://img.shields.io/github/stars/zzq0324/alarm-bot)
![Fork](https://img.shields.io/github/forks/zzq0324/alarm-bot)
![Issues](https://img.shields.io/github/issues/zzq0324/alarm-bot)
![Licence](https://img.shields.io/github/license/zzq0324/alarm-bot)

> Alarm Bot中文名"告警机器人"，通过三方平台的开放能力，完成告警 -> 创建群 -> 拉人进群 -> 解决告警的闭环。针对未及时处理的告警提醒相关人员及时跟进处理，确保不会遗漏。

演示、部署以及进阶等更多信息可以查看[详细文档](https://alarm-bot.zzq0324.cn/)。

## 特性

- 收到告警自动建群拉人，自动追踪排查进度；
- 记录每个告警详细信息，便于追踪、统计和分析；
- 支持设置每个项目的Owner和Member。

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
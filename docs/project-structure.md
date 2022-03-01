## 模块结构

```
alarm-bot-parent
|
|__ alarm-bot-core（核心代码，是一个Jar包，包含SPI、Extension和dao、service类）
|
|__ alarm-bot-web（HTTP服务，接收三方平台的事件回调以及相关定时任务）
|
|__ alarm-bot-backend（HTTP服务，提供后台配置界面相关接口和前端页面）
```

## 使用技术

- JAVA
  - SpringBoot
  - Mybatis Plus
  - FastJson
  - larksuite-oapi（飞书SDK）
  - Lombok
- 数据库：MySQL
- Maven
- 文档：[docsify](https://github.com/docsifyjs/docsify)，超好用
- 前端：[EasyUI](https://www.jeasyui.com/) （Freeware）
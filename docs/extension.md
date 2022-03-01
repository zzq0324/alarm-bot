## 为什么需要扩展节点？

> `Alarm Bot`定位是支持多平台，因此在设计支持就考虑了扩展性问题。结合SPI机制以及参照Dubbo的SPI实现，实现了自己的扩展节点。<br />通过扩展节点可以很方便的实现三方平台的切换、文件存储的切换以及支持更多的命令。

## 实现

**扩展节点定义**

扩展节点增加了自定义注解类`SPI.java`，定义扩展节点的时候需要注解`@SPI`。

[gist: SPI.java](https://raw.githubusercontent.com/zzq0324/alarm-bot/main/alarm-bot-core/src/main/java/cn/zzq0324/alarm/bot/core/spi/SPI.java ':include :type=code')


**实现节点注解**

实现节点中有个非常重要的属性`isDefault`，用于标识当前注解的实现节点是否是系统默认的实现节点。例如我们默认配置的告警平台是飞书，那么所有的推送、建群都是基于飞书产生。每个扩展节点有且仅有一个默认的实现节点。

[gist: Extension.java](https://raw.githubusercontent.com/zzq0324/alarm-bot/main/alarm-bot-core/src/main/java/cn/zzq0324/alarm/bot/core/spi/Extension.java ':include :type=code')

**实现节点加载器**

`ExtensionLoader`是实现节点的加载器，负责将对应扩展节点的实现节点初始化并缓存起来，以便于提升性能。

[gist: ExtensionLoader.java](https://raw.githubusercontent.com/zzq0324/alarm-bot/main/alarm-bot-core/src/main/java/cn/zzq0324/alarm/bot/core/spi/ExtensionLoader.java ':include :type=code')

## 自定义

看到这里，你一定会想，如果我想自定义实现节点，该如何做？很简单，仅需要两步：

- 新增一个类，继承对应的扩展节点接口（注意此时`isDefault`是false）；
- 在`application.properties`中增加一个配置项，格式为spi.+包名+类名，例如：`spi.cn.zzq0324.alarm.bot.core.spi.extension.DemoExtension`。

此时`ExtensionLoader`在加载的时候会默认加载自定义的实现节点，达到灵活替换的目的。使用中就可以非常方便，例如：

```java
ExtensionLoader.getDefaultExtension(PlatformExt.class).replyText(message.getThirdMessageId(), 'ExtensionLoader');
```
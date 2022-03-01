> 默认支持/create、/solve和/help指令，可以通过扩展支持更多的指令。

## 新增实现类

新增一个实现类，需要继承`Command`，例如：

```java
@Extension(name = "customEvent", summary = "指令-自定义")
public class CustomCommand implements Command{
    @Override
    public CommandContext matchCommand(IMMessage imMessage) {
        return null;
    }

    @Override
    public boolean execute(CommandContext context) {
        return false;
    }
}
```

## 增加配置信息

在`application.properties`（如果没有新增一个文件）中增加以下配置：

```
spi.cn.zzq0324.alarm.bot.core.extension.cmd.Command=customEvent
```
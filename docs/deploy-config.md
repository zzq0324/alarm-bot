!> **注意：**考虑到安全问题，`Alarm Bot`将项目拆分为`alarm-bot-web`和`alarm-bot-backend`，其中`alarm-bot-web`
用于接收公网回调，因此需要公网暴露；`alarm-bot-backend`用于后台管理需要，只需要内网访问即可。<br/><br/>切不可将`alarm-bot-backend`
部署到公网，那样有可能造成信息泄露。因此请在网络上进行安全限制，没限制带来的任何损失`Alarm Bot`不承担任何责任！！！

## 下载

**方式一：直接下载压缩包**

> 待完善，还需要研究下怎么通过`GitHub`打包。

**方式二：clone后编译打包**

```bash
# clone项目
git clone git@github.com:zzq0324/alarm-bot.git

# 进入目录
cd alram-bot

# 直接打包（确保已经安装Maven）
mvn clean package
```

打包后可以看到在`alarm-bot-web`和`alarm-bot-backend`目录下各生成一个zip包。

## 部署

* **创建数据库**

数据库脚本详见[init.sql](https://alarm-bot.zzq0324.cn/init.sql)，部署前请先创建相关表。

* **解压缩**

可以通过压缩软件或通过以下命令：

```bash
unzip xxx
```

解压后可以看到，两个服务的目录下都包含`bin`、`conf`和`lib`，其中`bin`存放启动命令，`conf`存放配置信息，`lib`是运行依赖的jar包。

* **修改配置**

```bash
# 切到配置目录
cd conf

# 编辑配置文件
vi application.yml

```

然后参照[配置说明](/config-desc)进行修改。

* **启动**

```bash
# 启动alarm-bot-web
cd alarm-bot-web
./bin/start.sh &

# 启动alarm-bot-backend
cd alarm-bot-backend
./bin/start.sh &
```

启动后观察日志，如果看到控制台输出`Started Start in`字样代表启动成功。完整日志如下：

```java
2022-03-01 13:08:50.812INFO[main][-]cn.zzq0324.alarm.bot.web.Start-Starting Start v1.1.0on VM-0-11-ubuntu with PID 5354(/home/ubuntu/alarm-bot-web/lib/alarm-bo t-web-1.1.0.jar started by ubuntu in/home/ubuntu/alarm-bot-web)
    2022-03-01 13:08:50.814INFO[main][-]cn.zzq0324.alarm.bot.web.Start-No active profile set,falling back to default profiles:default
2022-03-01 13:08:51.903INFO[main][-]o.s.b.w.embedded.tomcat.TomcatWebServer-Tomcat initialized with port(s):8888(http)
    2022-03-01 13:08:51.912INFO[main][-]o.a.coyote.http11.Http11NioProtocol-Initializing ProtocolHandler["http-nio-8888"]
    2022-03-01 13:08:51.913INFO[main][-]o.apache.catalina.core.StandardService-Starting service[Tomcat]
    2022-03-01 13:08:51.913INFO[main][-]org.apache.catalina.core.StandardEngine-Starting Servlet engine:[Apache Tomcat/9.0.36]
    2022-03-01 13:08:51.964INFO[main][-]o.a.c.c.C.[Tomcat].[localhost].[/]-Initializing Spring embedded WebApplicationContext 2022-03-01 13:08:51.965INFO[main][-]w.s.c.ServletWebServerApplicationContext-Root WebApplicationContext:initialization completed in 1107ms 2022-03-01 13:08:52.465WARN[main][-]c.b.m.core.metadata.TableInfoHelper-Can not find table primary key in Class:"cn.zzq0324.alarm.bot.core.entity.Lock". 2022-03-01 13:08:52.508INFO[main][-]c.z.a.b.core.spring.SpringContextHolder-set applicationContext to SpringContextHolder successfully. 2022-03-01 13:08:52.612INFO[main][-]o.s.s.concurrent.ThreadPoolTaskExecutor-Initializing ExecutorService'applicationTaskExecutor'
    2022-03-01 13:08:52.777INFO[main][-]o.s.s.c.ThreadPoolTaskScheduler-Initializing ExecutorService'taskScheduler'
    2022-03-01 13:08:52.797INFO[main][-]o.a.coyote.http11.Http11NioProtocol-Starting ProtocolHandler["http-nio-8888"]
    2022-03-01 13:08:52.816INFO[main][-]o.s.b.w.embedded.tomcat.TomcatWebServer-Tomcat started on port(s):8888(http)with context path''
    2022-03-01 13:08:52.828INFO[main][-]cn.zzq0324.alarm.bot.web.Start-Started Start in2.433seconds(JVM running for 2.73)
```

## 访问链接

- `alarm-bot-web`部署完成后，自动暴露`/lark/callback`接口用于接收飞书回调信息，例如：https://alarmbot.zzq0324.cn/lark/callback ；
- `alarm-bot-backend`部署完成后，可以直接通过访问，例如：https://backend-alarmbot.zzq0324.cn 。

## 常见Q&A

- **项目无法启动，该怎么办？**
    - 检查下JDK是否安装正确，通过`java -version`查看是否正常输出；
    - 检查下数据库连接是否正常配置。
- **为什么收不到回调信息？**
    - 检查下配置的回调地址是否正确；
    - 检查下配置信息是否正确，包含`app-id`、`app-secret`、`verify-token`、`encrypt-key`。
- **为什么我的告警群能收到告警，但是不会自动拉人？**
    - 检查下`alarm-bot-web`的`conf/application.yml`的配置项`project-name-reg-exp`是否正确，仅支持一对括号；
    - 后台页面检查下对应的项目是否没有配置Owner和Member。

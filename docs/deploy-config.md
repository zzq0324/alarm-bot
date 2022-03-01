### alarm-bot-backend
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


## 常见Q&A

* 为什么我的项目无法收到告警？
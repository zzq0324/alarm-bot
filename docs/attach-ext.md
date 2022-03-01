> 聊天中会有图片信息，因此需要下载下来存储到服务器上，方便后续追踪。默认实现放在本地，可以通过扩展支持阿里云OSS或者七牛云等。

## 新增实现类

新增一个实现类，需要继承`StorageExt`，例如：

```java
@Extension(name = "aliyunOssStorage", summary = "阿里云oss存储")
public class AliyunOssStorage implements StorageExt {

    @Override
    public String upload(byte[] data, String fileName) {
        return null;
    }
}
```

## 增加配置信息

在`application.properties`（如果没有新增一个文件）中增加以下配置：

```
spi.cn.zzq0324.alarm.bot.core.extension.storage.StorageExt=aliyunOssStorage
```
package cn.zzq0324.alarm.bot.spi;

import cn.zzq0324.alarm.bot.spring.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * description: 扩展节点加载器，参照dubbo官方的ExtensionLoader实现 <br>
 * date: 2022/2/5 8:24 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Slf4j
public class ExtensionLoader<T> {

    // 默认spi配置前缀
    private static final String DEFAULT_SPI_PROPERTIES_PREFIX = "spi.";

    // 所有的扩展节点加载器，初始化之后缓存起来，提高访问效率
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap(64);

    // key为扩展节点名称，value为对应的spring bean实例
    private ConcurrentHashMap<String, T> extensionMap = new ConcurrentHashMap<>();

    // 默认实现节点
    private T defaultExt = null;

    // 默认实现节点名称
    private String defaultExtName = null;

    private ExtensionLoader(Class<T> type) {
        Map<String, T> beanMap = SpringContextHolder.getContext().getBeansOfType(type);

        beanMap.entrySet().stream().forEach(entry -> {
            T extensionInstance = entry.getValue();

            Class<?> targetClass = AopUtils.getTargetClass(extensionInstance);

            // 未注解@Extension将忽略，不作为扩展实现节点使用
            if (!targetClass.isAnnotationPresent(Extension.class)) {
                throw new RuntimeException(targetClass.getName() + " not annotated with @Extension.");
            } else {
                Extension extension = targetClass.getAnnotation(Extension.class);

                checkDuplicateDefaultExt(extension, extensionInstance);

                extensionMap.putIfAbsent(extension.name(), extensionInstance);
            }
        });

        // 设置默认实现节点，人工设置比系统默认的优先级高
        setDefaultExtByConfig(type);
    }

    /**
     * 获取对应扩展节点的加载器，每个扩展节点有自己独立的加载器
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }

        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }

        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException(
                "Extension type (" + type + ") is not an extension, because it is not annotated with @"
                    + SPI.class.getSimpleName() + "!");
        }

        ExtensionLoader<T> loader = (ExtensionLoader)EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader(type));
            loader = (ExtensionLoader)EXTENSION_LOADERS.get(type);
        }

        return loader;
    }

    /**
     * 获取扩展实现节点实例
     */
    public static <T> T getExtension(Class<T> type, String extensionName) {
        return getExtension(type, extensionName, false);
    }

    /**
     * 获取扩展实现节点实例
     */
    public static <T> T getExtension(Class<T> type, String extensionName, boolean isCheck) {
        ExtensionLoader<T> extensionLoader = ExtensionLoader.getExtensionLoader(type);
        if (isCheck && !extensionLoader.extensionMap.containsKey(extensionName)) {
            throw new IllegalArgumentException("extension not exists, extensionName: " + extensionName);
        }

        return extensionLoader.extensionMap.get(extensionName);
    }

    /**
     * 返回默认实现节点
     */
    public static <T> T getDefaultExtension(Class<T> type) {
        return ExtensionLoader.getExtensionLoader(type).defaultExt;
    }

    /**
     * 返回默认实现节点名称
     */
    public static <T> String getDefaultExtensionName(Class<T> type) {
        return ExtensionLoader.getExtensionLoader(type).defaultExtName;
    }

    /**
     * 获取扩展节点列表
     */
    public <T> List<T> getExtensionList() {
        return new ArrayList<T>((Collection<? extends T>)extensionMap.values());
    }

    /**
     * 检测是否有1个以上的默认实现节点
     */
    private void checkDuplicateDefaultExt(Extension extension, T extensionInstance) {
        if (extension.isDefault()) {
            if (defaultExt != null) {
                throw new IllegalStateException("duplicate default extension found!");
            }

            defaultExt = extensionInstance;
            defaultExtName = extension.name();
        }
    }

    /**
     * 设置默认实现节点，如果有手工配置以人工配置调整的为准
     */
    private void setDefaultExtByConfig(Class<T> type) {
        // 获取默认的扩展节点名称，通过application.properties读取
        String propertyKey = DEFAULT_SPI_PROPERTIES_PREFIX + type.getTypeName();
        String extName = SpringContextHolder.getEnvironment().getProperty(propertyKey);

        if (extName != null) {
            defaultExt = SpringContextHolder.getContext().getBean(extName, type);

            if (defaultExt == null) {
                log.warn("default extension not exists, type: {}", type.getName());
            }

            defaultExtName = defaultExt == null ? null : extName;
        }
    }

    /**
     * 判断是否声明为扩展节点
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }
}


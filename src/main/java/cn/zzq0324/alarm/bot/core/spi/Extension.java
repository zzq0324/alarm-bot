package cn.zzq0324.alarm.bot.core.spi;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 声明@Extension的为扩展实现节点 <br>
 * date: 2022/2/4 4:08 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE})
@Component
public @interface Extension {

    /**
     * 扩展节点名称
     */
    String name();

    /**
     * 是否默认扩展节点
     */
    boolean isDefault() default false;

    /**
     * 扩展节点概述
     */
    String summary();
}

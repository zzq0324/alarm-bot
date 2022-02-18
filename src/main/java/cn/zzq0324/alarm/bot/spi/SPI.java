package cn.zzq0324.alarm.bot.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 声明@SPI的为扩展节点 <br>
 * date: 2022/2/4 4:06 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE})
public @interface SPI {

    /**
     * SPI名称
     *
     * @return
     */
    String value();
}

package cn.zzq0324.alarm.bot.core.extension.platform.impl.lark.parser;

import cn.zzq0324.alarm.bot.core.spi.SPI;
import cn.zzq0324.alarm.bot.core.vo.IMMessage;
import com.larksuite.oapi.service.im.v1.model.MessageReceiveEventData;

/**
 * description: 飞书消息解析 <br>
 * date: 2022/2/22 8:53 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SPI("larkMessageParserExt")
public interface LarkMessageParserExt {

    /**
     * 飞书IM消息解析
     */
    void parse(IMMessage imMessage, MessageReceiveEventData eventData);
}

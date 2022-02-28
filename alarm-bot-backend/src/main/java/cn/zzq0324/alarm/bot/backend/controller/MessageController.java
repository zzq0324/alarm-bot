package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.MessageRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.entity.Message;
import cn.zzq0324.alarm.bot.core.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: MessageController <br>
 * date: 2022/2/28 3:49 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping("/list")
    public Page list(MessageRequest request) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Message> page =
            messageService.listPage(request.getPage(), request.getRows(), request.getChatGroupId());

        return new Page(page);
    }
}

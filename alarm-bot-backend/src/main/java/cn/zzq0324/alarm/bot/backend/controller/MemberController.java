package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.MemberRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: BackendController <br>
 * date: 2022/2/25 10:56 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@RestController
@RequestMapping(("/member"))
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 获取成员分页
     */
    @GetMapping("/list")
    public Page list(MemberRequest request) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page =
            memberService.listPage(request.getPage(), request.getRows(), request.getName(), request.getMobile());

        return new Page(page);
    }
}

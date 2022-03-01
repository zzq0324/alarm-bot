package cn.zzq0324.alarm.bot.backend.controller;

import cn.zzq0324.alarm.bot.backend.request.MemberRequest;
import cn.zzq0324.alarm.bot.backend.response.Page;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
    @RequestMapping("/list")
    public Page list(MemberRequest request) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page =
            memberService.listPage(request.getPage(), request.getRows(), request.getName(), request.getIdentity(),
                request.getStatus());

        return new Page(page);
    }

    @RequestMapping("/getMemberByIdentify")
    public Member getMemberByIdentify(String identity) {
        Member member = memberService.getByIdentity(identity);
        if (member == null) {
            member = new Member();
            member.setIdentity(identity);

            memberService.setMemberThirdAuthInfo(member);
        }

        return member;
    }

    @RequestMapping("/add")
    public void addMember(String identify) {
        Member member = memberService.getByIdentity(identify);
        if (member != null) {
            return;
        }

        memberService.addMember(identify);
    }

    @RequestMapping("/updateStatus")
    public void updateStatus(int status, String ids) {
        Set<String> idSet = StringUtils.commaDelimitedListToSet(ids);

        idSet.stream().forEach(idStr -> {
            Member member = memberService.get(Long.parseLong(idStr));
            member.setStatus(status);

            memberService.update(member);
        });
    }
}

package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.dao.MemberDao;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.core.spi.ExtensionLoader;
import cn.zzq0324.alarm.bot.core.vo.MemberThirdAuthInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * description: MemberService <br>
 * date: 2022/2/19 9:02 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 添加成员
     */
    public void addMember(String identity) {
        Member member = getByIdentity(identity);
        // 根据手机号查找是否存在，存在并且有授权信息，则直接返回
        if (member != null) {
            return;
        }

        member = new Member();
        member.setIdentity(identity);
        member.setCreateTime(new Date());
        member.setStatus(Member.STATUS_NORMAL);

        setMemberThirdAuthInfo(member);

        memberDao.insert(member);
    }

    public Page<Member> listPage(int currentPage, int size, String name, String identity, int status) {
        Page page = new Page(currentPage, size);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasLength(name)) {
            queryWrapper.like("name", name);
        }

        if (StringUtils.hasLength(identity)) {
            queryWrapper.eq("identity", identity);
        }

        if (status >= 0) {
            queryWrapper.eq("status", status);
        }

        return memberDao.selectPage(page, queryWrapper);
    }

    /**
     * 设置成员三方授权信息
     */
    public void setMemberThirdAuthInfo(Member member) {
        // 为空重新从第三方调用获取信息，兼容你换IM的情况
        MemberThirdAuthInfo memberThirdAuthInfo =
            ExtensionLoader.getDefaultExtension(PlatformExt.class).getMemberInfo(member.getIdentity());

        if (memberThirdAuthInfo == null) {
            log.warn("can't find third platform account by identity: {}", member.getIdentity());

            return;
        }

        member.setName(memberThirdAuthInfo.getName());
        member.setOpenId(memberThirdAuthInfo.getOpenId());
        member.setUnionId(memberThirdAuthInfo.getUnionId());
    }

    public Member getByIdentity(String identity) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("identity", identity);

        return memberDao.selectOne(queryWrapper);
    }

    public Member get(Long id) {
        return get(id, true);
    }

    public Member get(Long id, boolean updateThirdAuthInfo) {
        Member member = memberDao.selectById(id);
        if (member == null) {
            log.warn("can't find member by id: {}", id);

            return null;
        }

        if (!updateThirdAuthInfo || StringUtils.hasLength(member.getOpenId())) {
            return member;
        }

        // openId和unionId为空，则调用三方进行设置更新
        // 主要是为了兼容换平台的场景，此时通过sql更新为null即可重新获取
        setMemberThirdAuthInfo(member);
        memberDao.updateById(member);

        return member;
    }

    public void update(Member member) {
        memberDao.updateById(member);
    }
}

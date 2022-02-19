package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.dao.MemberDao;
import cn.zzq0324.alarm.bot.dao.MemberPlatformInfoDao;
import cn.zzq0324.alarm.bot.entity.Member;
import cn.zzq0324.alarm.bot.entity.MemberPlatformInfo;
import cn.zzq0324.alarm.bot.extension.platform.PlatformExt;
import cn.zzq0324.alarm.bot.spi.ExtensionLoader;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: MemberService <br>
 * date: 2022/2/19 9:02 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberPlatformInfoDao memberPlatformInfoDao;

    /**
     * 添加成员
     */
    public void addMember(Member member) {
        // 根据手机号查找是否存在，存在不重复添加
        if (getByMobile(member.getMobile()) != null) {
            return;
        }

        // 插入member信息
        memberDao.insert(member);

        // 检测是否已经存在三方信息
        getMemberPlatformInfo(member, true);
    }

    public MemberPlatformInfo getMemberPlatformInfo(Member member, boolean addIfNotExists) {
        // 获取对应的三方信息
        String platform = ExtensionLoader.getDefaultExtensionName(PlatformExt.class);
        MemberPlatformInfo memberPlatformInfo = getMemberPlatformInfo(platform, member.getId());
        if (memberPlatformInfo != null) {
            return memberPlatformInfo;
        }

        // 为空重新从第三方调用获取信息，兼容你换IM的情况
        String mobile = member.getMobile();
        memberPlatformInfo = ExtensionLoader.getDefaultExtension(PlatformExt.class).getMemberInfo(mobile);
        if (memberPlatformInfo == null) {
            throw new RuntimeException("根据手机号" + mobile + "查找不到对应的三方账号信息");
        }

        memberPlatformInfo.setMemberId(member.getId());
        memberPlatformInfo.setPlatform(platform);
        // 插入三方关系信息表
        memberPlatformInfoDao.insert(memberPlatformInfo);

        return memberPlatformInfo;
    }

    public MemberPlatformInfo getMemberPlatformInfo(String platform, Long memberId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("platform", platform);
        queryWrapper.eq("member_id", memberId);

        return memberPlatformInfoDao.selectOne(queryWrapper);
    }

    public Member getByMobile(String mobile) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);

        return memberDao.selectOne(queryWrapper);
    }

    public Member get(Long id) {
        return memberDao.selectById(id);
    }
}

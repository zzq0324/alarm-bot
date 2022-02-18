package cn.zzq0324.alarm.bot.constant;

/**
 * description: 关系类型 <br>
 * date: 2022/2/16 10:54 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
public interface RelationType {

    /**
     * 项目和成员的关系
     */
    String PROJECT_MEMBER_RELATION = "project-member";

    /**
     * 项目成员和三方平台的关系，例如A用户在钉钉上有OpenID，在飞书上有OpenID，就存在两条关系
     */
    String MEMBER_THIRD_RELATION = "member-third";
}

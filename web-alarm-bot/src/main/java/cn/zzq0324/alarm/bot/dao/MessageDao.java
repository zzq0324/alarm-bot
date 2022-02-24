package cn.zzq0324.alarm.bot.dao;

import cn.zzq0324.alarm.bot.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * description: MessageDao <br>
 * date: 2022/2/16 3:07 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {
}

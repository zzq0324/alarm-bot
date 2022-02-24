package cn.zzq0324.alarm.bot.core.dao;

import cn.zzq0324.alarm.bot.core.entity.EventLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * description: EventLogDao <br>
 * date: 2022/2/15 7:15 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Repository
public interface EventLogDao extends BaseMapper<EventLog> {
}

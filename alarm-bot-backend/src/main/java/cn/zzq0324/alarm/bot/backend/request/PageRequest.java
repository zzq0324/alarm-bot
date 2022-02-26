package cn.zzq0324.alarm.bot.backend.request;

import lombok.Data;

/**
 * description: PageRequest <br>
 * date: 2022/2/26 10:27 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
public class PageRequest {

    /**
     * 分页
     */
    int page = 1;

    /**
     * 每页记录数
     */
    int rows = 20;
}

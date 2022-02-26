package cn.zzq0324.alarm.bot.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * description: Page <br>
 * date: 2022/2/26 10:11 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Data
@AllArgsConstructor
public class Page {

    /**
     * 记录总数
     */
    private long total;

    /**
     * 详细记录
     */
    private List rows;

    public Page(com.baomidou.mybatisplus.extension.plugins.pagination.Page page){
        this.rows = page.getRecords();
        this.total = page.getTotal();
    }
}

package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.dao.ProjectDao;
import cn.zzq0324.alarm.bot.entity.Project;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: ProjectService <br>
 * date: 2022/2/19 11:16 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    public Project getByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);

        return projectDao.selectOne(queryWrapper);
    }

    public Project getById(long id) {
        return projectDao.selectById(id);
    }
}

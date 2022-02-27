package cn.zzq0324.alarm.bot.core.service;

import cn.zzq0324.alarm.bot.core.dao.ProjectDao;
import cn.zzq0324.alarm.bot.core.entity.Member;
import cn.zzq0324.alarm.bot.core.entity.Project;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public void update(Project project) {
        projectDao.updateById(project);
    }

    public void add(Project project) {
        projectDao.insert(project);
    }

    public Project getByName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);

        return projectDao.selectOne(queryWrapper);
    }

    public Page<Member> listPage(int currentPage, int size, String projectName, int status, String ownerName) {
        Page page = new Page(currentPage, size);
        QueryWrapper queryWrapper = new QueryWrapper();

        if (StringUtils.hasLength(projectName)) {
            queryWrapper.like("name", projectName);
        }

        if (StringUtils.hasLength(ownerName)) {
            queryWrapper.like("owner_name", ownerName);
        }

        if (status >= 0) {
            queryWrapper.eq("status", status);
        }

        return projectDao.selectPage(page, queryWrapper);
    }

    public Project getById(long id) {
        return projectDao.selectById(id);
    }
}

package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.dao.RelationDao;
import cn.zzq0324.alarm.bot.entity.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: RelationService <br>
 * date: 2022/2/15 9:29 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@Service
public class RelationService {

    @Autowired
    private RelationDao relationDao;

    /**
     * 设置关系，移除旧的关系，保存
     *
     * @param sourceId   源ID
     * @param type       类型
     * @param destIdList 目标ID列表
     */
    public void setRelation(Long sourceId, String type, List<Long> destIdList) {
        // 移除旧的绑定关系
        remove(sourceId, type);

        if (CollectionUtils.isEmpty(destIdList)) {
            return;
        }

        for (Long destId : destIdList) {
            Relation relation = new Relation();
            relation.setType(type);
            relation.setDestId(destId);
            relation.setSourceId(sourceId);

            relationDao.insert(relation);
        }
    }

    /**
     * 移除关系
     *
     * @param sourceId 源ID
     * @param type     类型
     */
    public void remove(Long sourceId, String type) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("source_id", sourceId);
        paramMap.put("type", type);

        relationDao.deleteByMap(paramMap);
    }
}

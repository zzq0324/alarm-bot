package cn.zzq0324.alarm.bot.dao;

import cn.zzq0324.alarm.bot.constant.RelationType;
import cn.zzq0324.alarm.bot.entity.Relation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description: RelationDaoTest <br>
 * date: 2022/2/12 10:59 上午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class RelationDaoTest {

    @Autowired
    private RelationDao relationDao;

    @Test
    public void testNotNull(){
        Assertions.assertNotNull(relationDao);
    }

    @Test
    public void testInsert(){
        Relation relation = new Relation();
        relation.setType(RelationType.PROJECT_MEMBER_RELATION);
        relation.setSourceId(1L);
        relation.setDestId(100L);

        relationDao.insert(relation);

        Assertions.assertTrue(relation.getId()>0);
    }
}

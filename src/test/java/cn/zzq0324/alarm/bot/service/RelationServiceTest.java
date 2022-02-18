package cn.zzq0324.alarm.bot.service;

import cn.zzq0324.alarm.bot.constant.RelationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * description: RelationServiceTest <br>
 * date: 2022/2/16 12:42 下午 <br>
 * author: zzq0324 <br>
 * version: 1.0 <br>
 */
@SpringBootTest
public class RelationServiceTest {

    @Autowired
    private RelationService relationService;

    @Test
    public void testRemove() {
        relationService.remove(1L, RelationType.PROJECT_MEMBER_RELATION);
    }

    @Test
    public void testSetRelation() {
        relationService.setRelation(2L, RelationType.PROJECT_MEMBER_RELATION, Arrays.asList(1L, 2L));
    }
}

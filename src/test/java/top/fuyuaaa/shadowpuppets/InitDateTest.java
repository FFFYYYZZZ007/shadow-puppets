package top.fuyuaaa.shadowpuppets;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.fuyuaaa.shadowpuppets.dao.CategoryDao;
import top.fuyuaaa.shadowpuppets.dao.CourseDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.util.UUIDUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 20:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InitDateTest {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GoodsOrderDao goodsOrderDao;

    @Test
    public void insertCategory() {
        Category category1 = new Category();
        category1.setCategoryName("纪念品");
        categoryDao.insertCategory(category1);
        Category category2 = new Category();
        category2.setCategoryName("收藏品");
        categoryDao.insertCategory(category2);
    }

    @Test
    public void insertUser() {
        for (int i = 0; i < 30; i++) {
            UserPO userPO = new UserPO();
            userPO.setUserName("userName" + i);
            userPO.setPassword("password" + i);
            userPO.setBirthday(new Date());
            userPO.setTel("tel" + i);
            userPO.setSex(i % 2 == 0 ? 1 : 0);
            userDao.insert(userPO);
        }
    }

    @Test
    public void insertOrder() {

        List<GoodsOrderPO> orderList = goodsOrderDao.getOrderList(new GoodsOrderQO());
        orderList.forEach(goodsOrderPO -> {
            goodsOrderPO.setId(UUIDUtils.getOrderCode());
            goodsOrderPO.setDateCreate(new Date(goodsOrderPO.getDateCreate().getTime() - 1000 * 60 * 60 * 24 * 7));
            goodsOrderDao.insertNewGoodsOrder2(goodsOrderPO);
        });
    }

    @Autowired
    CourseDao courseDao;

    @Test
    public void testInsertCourse() {
        for (int i = 0; i < 30; i++) {
            CoursePO coursePO = new CoursePO("皮影戏课程名" + i + 1, "课程介绍" + i + 1,"http://fuyuaaa-bucket.oss-cn-hangzhou.aliyuncs.com/pics/courses/xx.PNG", new BigDecimal(12 + i + 1), new BigDecimal(11 + i + 1), "傅宇", 12 + i + 1, 120 + (i + 1) * 5, "地点" + i + 1, "很丰富");
            courseDao.insert(coursePO);
            System.out.println(coursePO);
        }
    }
}

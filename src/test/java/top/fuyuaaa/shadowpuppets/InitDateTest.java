package top.fuyuaaa.shadowpuppets;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.fuyuaaa.shadowpuppets.dao.CategoryDao;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;

import java.util.Date;

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
}

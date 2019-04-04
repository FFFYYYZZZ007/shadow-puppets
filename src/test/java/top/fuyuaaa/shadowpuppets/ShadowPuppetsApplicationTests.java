package top.fuyuaaa.shadowpuppets;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.fuyuaaa.shadowpuppets.dao.UserDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShadowPuppetsApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetById(){
        log.info(userDao.getById(1).toString());
        log.info(userDao.getByUserName("fuyu").toString());
    }
}

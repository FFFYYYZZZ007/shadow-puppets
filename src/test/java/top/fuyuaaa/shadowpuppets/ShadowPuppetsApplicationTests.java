package top.fuyuaaa.shadowpuppets;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.fuyuaaa.shadowpuppets.dao.ExpressDeliveryDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsCommentDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.mapstruct.CommentConverter;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsCommentVO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShadowPuppetsApplicationTests {

    @Autowired
    UserDao userDao;

    @Autowired
    GoodsDao goodsDao;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetById() {
        log.info(userDao.getById(1).toString());
        log.info(userDao.getByUserName("fuyu").toString());
    }

    @Test
    public void testGetList() {
        log.info(goodsDao.findList(new GoodsListQO()).toString());
        log.info(goodsDao.findByGoodsId(1).toString());
    }

    @Test
    public void testx() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 200000; i++) {
            list.add(String.valueOf(i));
        }
        Iterable<List<String>> parts = Iterables.partition(list, 1000);
        parts.forEach(userDao::insertBatch);

        List<String> list2 = new ArrayList<>();
        parts.forEach(list2::addAll);
        System.out.println(list.size());
        System.out.println(list2.size());
    }

    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;
    @Test
    public void test(){
        UserListQO userListQO = new UserListQO();
        userListQO.setPageNum(1);
        userListQO.setPageSize(10);
        System.out.println(JSON.toJSONString(userService.getUserManagerList(userListQO)));
        userListQO.setKeyword("xxx");
        System.out.println(JSON.toJSONString(userService.getUserManagerList(userListQO)));
        userListQO.setKeyword("fuyu");
        System.out.println(JSON.toJSONString(userService.getUserManagerList(userListQO)));

        goodsService.categoryStatisticsInfo();
    }

    @Autowired
    ExpressDeliveryDao expressDeliveryDao;
    @Test
    public void testEnumInsert(){
        System.out.println(JSON.toJSONString(expressDeliveryDao.findList(new ExpressDeliveryQO())));
    }

    @Autowired
    GoodsCommentDao goodsCommentDao;
    @Test
    public void testGoodsCommentDao(){
    }
}

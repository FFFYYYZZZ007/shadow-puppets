package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.UserSexEnum;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.analysis.NameValue;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-29 10:19
 */
@RestController
@RequestMapping("/user/manager")
@Slf4j
public class UserManagerController {

    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @PostMapping("/list")
    @ValidateAdmin
    public Result<PageVO<UserVO>> getUserManagerList(@RequestBody UserListQO userListQO) {
        fillQueryParam(userListQO);
        PageVO<UserVO> userManagerList = userService.getUserManagerList(userListQO);
        return Result.success(userManagerList);
    }

    @PostMapping("/remove")
    @ValidateAdmin
    public Result removeUserByManager(@RequestParam Integer userId) {
        Boolean success = userService.removeUser(userId);
        return success? Result.success().setMsg("移除用户成功"):Result.fail("移除用户失败");
    }

    @GetMapping("/header")
    @ValidateAdmin
    public Result<Map<String,Object>> userManagerHeader(){
        Integer boysNumber = userDao.countBySex(UserSexEnum.MONKEY_BOY.code());
        Integer girlsNumber = userDao.countBySex(UserSexEnum.MONKEY_GIRL.code());
        Integer boysPercent = boysNumber*100 / (boysNumber + girlsNumber);
        Integer girlsPercent = 100 - boysPercent;
        Integer newUserWeek = userDao.countNewUserWeek();
        Integer newUserMonth = userDao.countNewUserMonth();
        List<NameValue> userTagList = userDao.getUserTagList();
        userTagList.forEach(user->{
            user.setValue(RandomUtils.code2());
        });
        Map<String, Object> result = new HashMap<>();
        result.put("boysPercent", boysPercent);
        result.put("girlsPercent", girlsPercent);
        result.put("newUserWeek", newUserWeek);
        result.put("newUserMonth", newUserMonth);
        result.put("userTagList", userTagList);
        return Result.success(result);
    }

    /**
     * 填充分页参数(如果没有分页参数)
     *
     * @param userListQO 用户列表查询对象
     */
    private void fillQueryParam(UserListQO userListQO) {
        Integer pageNum = userListQO.getPageNum();
        Integer pageSize = userListQO.getPageSize();
        if (pageNum == null || pageNum <= 0) {
            userListQO.setPageNum(1);
        }
        if (pageSize == null || pageSize <= 0) {
            userListQO.setPageSize(10);
        }
    }
}

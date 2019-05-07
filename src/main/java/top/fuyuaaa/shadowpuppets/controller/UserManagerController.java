package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;

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
    UserService userService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @PostMapping("/list")
    @ValidateAdmin
    public Result<PageVO<UserVO>> getUserManagerList(@RequestBody UserListQO userListQO) {
        PageVO<UserVO> userManagerList = userService.getUserManagerList(userListQO);
        return Result.success(userManagerList);
    }

    @PostMapping("/remove")
    @ValidateAdmin
    public Result removeUserByManager(@RequestParam Integer userId) {
        userService.removeUser(userId);
        return Result.success().setMsg("移除用户成功");
    }

    @GetMapping("/header")
    @ValidateAdmin
    public Result<Map<String,Object>> userManagerHeader(){
        Map<String, Object> userManagerHeader = userService.getUserManagerHeader();
        return Result.success(userManagerHeader);
    }

}

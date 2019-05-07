package top.fuyuaaa.shadowpuppets.mapstruct;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;

import java.text.ParseException;
import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-05 09:34
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class UserConverter {
    public final static UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    public abstract LoginUserInfo toLoginUserInfo (UserBO userBO);

    public abstract LoginUserInfo toLoginUserInfo (UserPO userPO);

    public abstract UserPO toUserPO (UserBO userBO);

    public abstract UserBO toUserBO (UserPO userPO);

    //==============================  VO => BO  ==============================

    @Mapping(target = "sex", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    public abstract UserBO toUserBO (UserVO userVO);

    @AfterMapping
    public void afterToUserBO(UserVO userVO, @MappingTarget UserBO userBO) {
        userBO.setSex("男".equals(userVO.getSex()) ? 1 : 0);
        try {
            userBO.setBirthday(DateUtils.parseDate(userVO.getBirthday(), "yyyy-MM-dd"));
        } catch (ParseException e) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
    }

    //==============================  PO => VO  ==============================

    @Mapping(target = "sex", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    public abstract UserVO toUserVO (UserPO userPO);

    @AfterMapping
    public void afterToUserVO(UserPO userPO, @MappingTarget UserVO userVO) {
        userVO.setSex(userPO.getBirthday() != null ? (1 == userPO.getSex() ? "男" : "女") : "");
        userVO.setBirthday(userPO.getBirthday() != null ? DateFormatUtils.format(userPO.getBirthday(), "yyyy-MM-dd") : "暂无");
    }

    @IterableMapping(elementTargetType = UserVO.class)
    public abstract List<UserVO> toUserVOList(List<UserPO> userPOList);
}


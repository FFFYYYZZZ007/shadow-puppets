package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsCommentVO;
import top.fuyuaaa.shadowpuppets.common.utils.DateUtils;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 21:59
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class CommentConverter {
    public final static CommentConverter INSTANCE = Mappers.getMapper(CommentConverter.class);

    @Mapping(target = "dateCreate",ignore = true)
    public abstract GoodsCommentVO toGoodsCommentVO(GoodsCommentPO goodsCommentPO);

    @AfterMapping
    protected void afterToGoodsCommentVO(GoodsCommentPO source, @MappingTarget GoodsCommentVO target){
        target.setDateCreate(DateUtils.formatDate(source.getDateCreate()));
    }

    @IterableMapping(elementTargetType = GoodsCommentVO.class)
    public abstract List<GoodsCommentVO> toGoodsCommentVOList(List<GoodsCommentPO> sourceList);

}

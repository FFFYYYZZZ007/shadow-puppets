package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.vo.CategoryVO;
import top.fuyuaaa.shadowpuppets.common.utils.DateUtils;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-05 09:34
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class CategoryConverter {
    public final static CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);

    @Mapping(target = "dateUpdate",ignore = true)
    public abstract CategoryVO toCategoryVO (Category category);

    @AfterMapping
    public void afterCategoryVO(Category source, @MappingTarget CategoryVO target){
        target.setDateUpdate(DateUtils.formatDate(source.getDateUpdate()));
    }

    @IterableMapping(elementTargetType = CategoryVO.class)
    public abstract List<CategoryVO> toCategoryVOList(List<Category> sourceList);
}

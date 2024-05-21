package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.persistence.entity.Category;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {Category.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "categoryId", target = "categoryId", qualifiedByName = "getCategoryId")
    PostDto toDto(Post post);

    @Mapping(source = "categoryId", target = "categoryId", qualifiedByName = "categoryIdToCategory")
    Post toEntity(PostDto dto);

    @Named("getCategoryId")
    default Long getCategoryId(Category category) {
        return category.getId();
    }

    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId){
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
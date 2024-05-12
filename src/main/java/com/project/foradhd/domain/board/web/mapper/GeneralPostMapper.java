package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.board.persistence.entity.Category;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {Category.class})
public interface GeneralPostMapper {

    GeneralPostMapper INSTANCE = Mappers.getMapper(GeneralPostMapper.class);

    @Mapping(source = "categoryId", target = "categoryId", qualifiedByName = "getCategoryId")
    GeneralPostDto toDto(GeneralPost post);

    @Mapping(source = "categoryId", target = "categoryId", qualifiedByName = "categoryIdToCategory")
    GeneralPost toEntity(GeneralPostDto dto);

    @Named("getCategoryId")
    default Long getCategoryId(Category category) {
        return category.getCategoryId();
    }

    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId){
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setCategoryId(categoryId);
        return category;
    }
}
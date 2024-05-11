package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GeneralPostMapper {

    GeneralPostMapper INSTANCE = Mappers.getMapper(GeneralPostMapper.class);

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "writerId", source = "writer.writerId")
    @Mapping(target = "writerName", source = "writerName")
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "categoryName")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "anonymous", source = "anonymous")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "commentCount", source = "commentCount")
    @Mapping(target = "scrapCount", source = "scrapCount")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "lastModifiedAt", source = "lastModifiedAt")
    GeneralPostDto toDto(GeneralPost post);

    @Mapping(target = "writer.writerId", source = "writerId")
    @Mapping(target = "category.categoryId", source = "categoryId")
    @Mapping(target = "writerName", ignore = true)  // Assuming no direct mapping as it should be fetched from the user entity
    @Mapping(target = "categoryName", ignore = true) // Assuming no direct mapping as it should be fetched from the category entity
    GeneralPost toEntity(GeneralPostDto postDto);
}

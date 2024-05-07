package com.project.foradhd.domain.board.web.mapper;

import lombok.Getter;
import lombok.Setter;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface GeneralPostMapper {
    @Mappings({
            @Mapping(target = "postId", source = "generalPost.postId"),
            @Mapping(target = "writerId", source = "generalPost.writerId"),
            @Mapping(target = "categoryId", source = "generalPost.categoryId"),
            @Mapping(target = "writerName", source = "generalPost.writerName"),
            @Mapping(target = "categoryName", source = "generalPost.categoryName"),
            @Mapping(target = "title", source = "generalPost.title"),
            @Mapping(target = "content", source = "generalPost.content"),
            @Mapping(target = "anonymous", source = "generalPost.anonymous"),
            @Mapping(target = "images", source = "generalPost.images"),
            @Mapping(target = "likeCount", source = "generalPost.likeCount"),
            @Mapping(target = "commentCount", source = "generalPost.commentCount"),
            @Mapping(target = "scrapCount", source = "generalPost.scrapCount"),
            @Mapping(target = "viewCount", source = "generalPost.viewCount"),
            @Mapping(target = "tags", source = "generalPost.tags"),
            @Mapping(target = "createdAt", source = "generalPost.createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "lastModifiedAt", source = "generalPost.lastModifiedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    GeneralPostDto toDto(GeneralPost generalPost);

    @InheritInverseConfiguration
    GeneralPost toEntity(GeneralPostDto generalPostDto);
}
package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostScrapFilterMapper {

    PostScrapFilterMapper INSTANCE = Mappers.getMapper(PostScrapFilterMapper.class);

    @Mapping(target = "scrapId", source = "scrapId")
    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "createdAt", source = "createdAt")
    PostScrapFilterDto toDto(PostScrapFilter postScrapFilter);

    @Mapping(target = "scrapId", source = "scrapId")
    @Mapping(target = "post.postId", source = "postId")
    @Mapping(target = "user.userId", source = "userId")
    @Mapping(target = "createdAt", source = "createdAt")
    PostScrapFilter toEntity(PostScrapFilterDto postScrapFilterDto);
}

package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto(Post post);
    Post toEntity(PostRequestDto dto);
    PostResponseDto responsetoDto(Post post);
}
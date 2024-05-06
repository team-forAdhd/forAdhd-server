package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostScrapMapper {

    PostScrapDto toDto(PostScrap postScrap);
    PostScrap toEntity(PostScrapDto postScrapDto);
}

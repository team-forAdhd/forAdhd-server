package com.project.foradhd.domain.board.web.mapper;

import org.mapstruct.Mapper;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;

@Mapper(componentModel = "spring")
public interface GeneralPostMapper {

    GeneralPostDto toDto(GeneralPost generalPost);
    GeneralPost toEntity(GeneralPostDto generalPostDto);
}

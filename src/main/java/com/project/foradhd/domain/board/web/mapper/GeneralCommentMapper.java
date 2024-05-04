package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.GeneralComment;
import com.project.foradhd.domain.board.web.dto.GeneralCommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeneralCommentMapper {
    GeneralCommentDto toDto(GeneralComment generalComment);
    GeneralComment toEntity(GeneralCommentDto generalCommentDto);
}

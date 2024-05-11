package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "commentId", source = "commentId")
    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "writerId", source = "writer.writerId")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "anonymous", source = "anonymous")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "lastModifiedAt", source = "lastModifiedAt")
    CommentDto toDto(Comment comment);

    @Mapping(target = "post.postId", source = "postId")
    @Mapping(target = "writer.writerId", source = "writerId")
    @Mapping(target = "commentId", source = "commentId")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "anonymous", source = "anonymous")
    @Mapping(target = "likeCount", source = "likeCount")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "lastModifiedAt", source = "lastModifiedAt")
    Comment toEntity(CommentDto commentDto);

    void updateCommentFromDto(CommentDto commentDto, @MappingTarget Comment comment);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setCommentId(id);
        return comment;
    }
}

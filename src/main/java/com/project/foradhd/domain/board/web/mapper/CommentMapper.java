package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.mapstruct.*;
import com.project.foradhd.domain.board.web.dto.CommentDto;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {UserRepository.class, GeneralPost.class})
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "postId", target = "postId", qualifiedByName = "getPostId")
    CommentDto toDto(Comment entity);

    @Mapping(source = "postId", target = "postId", qualifiedByName = "postIdToPost")
    Comment toEntity(CommentDto dto);

    @Mapping(source = "postId", target = "postId", qualifiedByName = "postIdToPost")
    void updateCommentFromDto(CommentDto dto, @MappingTarget Comment entity);

    @Named("postIdToPost")
    default GeneralPost postIdToPost(Long postId) {
        if (postId == null) {
            return null;
        }
        GeneralPost post = new GeneralPost();
        post.setPostId(postId);
        return post;
    }

    @Named("getPostId")
    default Long getPostId(GeneralPost post) {
        return post != null ? post.getPostId() : null;
    }
}
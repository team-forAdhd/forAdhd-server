package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import org.mapstruct.*;
import com.project.foradhd.domain.board.web.dto.CommentDto;

@Mapper(componentModel = "spring", uses = {UserRepository.class, Post.class})
public interface CommentMapper {
    @Mapping(source = "postId", target = "postId", qualifiedByName = "getPostId")
    CommentDto toDto(Comment entity);

    @Mapping(source = "postId", target = "postId", qualifiedByName = "postIdToPost")
    Comment toEntity(CommentDto dto);

    @Mapping(source = "postId", target = "postId", qualifiedByName = "postIdToPost")
    void updateCommentFromDto(CommentDto dto, @MappingTarget Comment entity);

    @Named("postIdToPost")
    default Post postIdToPost(Long postId) {
        if (postId == null) {
            return null;
        }
        Post post = new Post();
        post.setId(postId);
        return post;
    }

    @Named("getPostId")
    default Long getPostId(Post post) {
        return post != null ? post.getId() : null;
    }
}
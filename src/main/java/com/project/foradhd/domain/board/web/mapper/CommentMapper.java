package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface CommentMapper {

    @Mapping(source = "post", target = "postId")
    @Mapping(source = "user", target = "userId")
    CommentResponseDto toDto(Comment comment);

    @Mapping(source = "postId", target = "post")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    Comment toEntity(CreateCommentRequestDto requestDto);
    default Long map(Post post) {
        return post == null ? null : post.getId();
    }

    default Post map(Long postId) {
        if (postId == null) {
            return null;
        }
        Post post = new Post();
        post.setId(postId);
        return post;
    }

    default String map(User user) {
        return user != null ? user.getId() : null;
    }
}

package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.user.business.service.UserService;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserService.class, CommentMapper.class})
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "childComments", target = "children", qualifiedByName = "mapChildComments")
    @Mapping(source = "parentComment", target = "parentComment", qualifiedByName = "mapParentCommentToDto")
    CommentResponseDto toDto(Comment comment);

    @Mapping(source = "postId", target = "post")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapParentComment")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    @Mapping(target = "writerId", ignore = true)
    Comment toEntity(CreateCommentRequestDto requestDto);

    default Long map(Post post) {
        return post == null ? null : post.getId();
    }

    default Post map(Long postId) {
        return postId == null ? null : Post.builder().id(postId).build();
    }

    default String map(User user) {
        return user != null ? user.getId() : null;
    }

    default User map(String userId) {
        return userId == null ? null : User.builder().id(userId).build();
    }

    @Named("mapUser")
    default User mapUser(String userId) {
        return userId == null ? null : User.builder().id(userId).build();
    }

    @Named("mapParentComment")
    default Comment mapParentComment(Long parentCommentId) {
        return parentCommentId == null ? null : Comment.builder().id(parentCommentId).build();
    }

    @Named("mapParentCommentToDto")
    default CommentResponseDto mapParentCommentToDto(Comment parentComment) {
        if (parentComment == null) return null;
        return CommentResponseDto.builder()
                .id(parentComment.getId())
                .content(parentComment.getContent())
                .userId(parentComment.getUser() != null ? parentComment.getUser().getId() : null)
                .postId(parentComment.getPost() != null ? parentComment.getPost().getId() : null)
                .anonymous(parentComment.isAnonymous())
                .likeCount(parentComment.getLikeCount())
                .nickname(parentComment.getNickname())
                .profileImage(parentComment.getProfileImage())
                .build();
    }

    @Named("mapChildComments")
    default List<CommentResponseDto> mapChildComments(List<Comment> childComments) {
        if (childComments == null) return null;
        return childComments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "postId", target = "post", qualifiedByName = "mapPost")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapParentComment")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    @Mapping(target = "likeCount", constant = "0L")
    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    Comment createCommentRequestDtoToComment(CreateCommentRequestDto createCommentRequestDto, @Context String userId);

    @AfterMapping
    default void setUserProfileFields(@MappingTarget Comment.CommentBuilder commentBuilder, @Context String userId, @Context UserProfileRepository userProfileRepository) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElse(null);
        if (userProfile != null) {
            commentBuilder.nickname(userProfile.getNickname());
            commentBuilder.profileImage(userProfile.getProfileImage());
        }
        commentBuilder.build();
    }

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "parentComment", target = "parentComment")
    @Mapping(source = "childComments", target = "children")
    CommentResponseDto.CommentListResponseDto commentToCommentListResponseDto(Comment comment);

    default CommentResponseDto toResponseDto(List<Comment> comments, PagingResponse paging) {
        List<CommentResponseDto.CommentListResponseDto> commentList = comments.stream()
                .map(this::commentToCommentListResponseDto)
                .collect(Collectors.toList());
        return CommentResponseDto.builder()
                .commentList(commentList)
                .paging(paging)
                .build();
    }

    @Named("mapPost")
    static Post mapPost(Long postId) {
        if (postId == null) {
            return null;
        }
        return Post.builder().id(postId).build();
    }

    @Named("mapUser")
    static User mapUser(String userId) {
        if (userId == null) {
            return null;
        }
        return User.builder().id(userId).build();
    }

    @Named("mapParentComment")
    static Comment mapParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return Comment.builder().id(parentCommentId).build();
    }

    default List<CommentResponseDto.CommentListResponseDto> mapCommentList(List<Comment> comments) {
        if (comments == null) return null;
        return comments.stream()
                .map(this::commentToCommentListResponseDto)
                .collect(Collectors.toList());
    }
}

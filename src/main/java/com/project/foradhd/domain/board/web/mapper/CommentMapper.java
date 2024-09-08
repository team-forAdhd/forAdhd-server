package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
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
    @Mapping(target = "user", ignore = true)
    Comment createCommentRequestDtoToComment(CreateCommentRequestDto createCommentRequestDto, @Context String userId);

    @AfterMapping
    default void setUserProfileFields(@MappingTarget Comment.CommentBuilder commentBuilder, @Context String userId, @Context UserProfileRepository userProfileRepository, CreateCommentRequestDto createCommentRequestDto) {
        if (!createCommentRequestDto.isAnonymous()) {
            UserProfile userProfile = userProfileRepository.findByUserId(userId).orElse(null);
            if (userProfile != null) {
                commentBuilder.nickname(userProfile.getNickname());
                commentBuilder.profileImage(userProfile.getProfileImage());
            }
        }
        commentBuilder.user(User.builder().id(userId).build());
    }

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    @Mapping(source = "childComments", target = "children", qualifiedByName = "mapChildComments")
    CommentResponseDto.CommentListResponseDto commentToCommentListResponseDto(Comment comment);

    default CommentResponseDto.CommentListResponseDto commentToCommentListResponseDtoWithChildren(Comment comment) {
        if (comment.getParentComment() != null) { // 자식 댓글인 경우
            return commentToCommentListResponseDto(comment);
        } else { // 부모 댓글인 경우
            return CommentResponseDto.CommentListResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .userId(comment.getUser().getId())
                    .postId(comment.getPost().getId())
                    .anonymous(comment.isAnonymous())
                    .likeCount(comment.getLikeCount())
                    .createdAt(comment.getCreatedAt())
                    .lastModifiedAt(comment.getLastModifiedAt())
                    .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                    .children(comment.getChildComments().stream()
                            .map(this::commentToCommentListResponseDto)
                            .collect(Collectors.toList()))
                    .nickname(comment.getNickname())
                    .profileImage(comment.getProfileImage())
                    .build();
        }
    }

    default CommentResponseDto toResponseDto(List<Comment> comments, PagingResponse paging) {
        List<CommentResponseDto.CommentListResponseDto> commentList = comments.stream()
                .map(this::commentToCommentListResponseDtoWithChildren)
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

    @Named("mapParentComment")
    static Comment mapParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return Comment.builder().id(parentCommentId).build();
    }

    @Named("mapUser")
    static User mapUser(String userId, @Context UserRepository userRepository) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Named("mapChildComments")
    default List<CommentResponseDto.CommentListResponseDto> mapChildComments(List<Comment> childComments) {
        if (childComments == null) {
            return null;
        }
        return childComments.stream()
                .map(this::commentToCommentListResponseDto)
                .collect(Collectors.toList());
    }

}

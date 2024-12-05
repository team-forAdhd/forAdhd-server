package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentListResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "postId", target = "post", qualifiedByName = "mapPost")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapParentComment")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    @Mapping(target = "likeCount", constant = "0")
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
    @Mapping(target = "children", expression = "java(mapChildComments(comment.getChildComments(), blockedUserIdList))")
    CommentListResponseDto.CommentResponseDto commentToCommentResponseDto(Comment comment, @Context List<String> blockedUserIdList);

    @AfterMapping
    default void setIsBlockedComment(@MappingTarget CommentListResponseDto.CommentResponseDto.CommentResponseDtoBuilder dto, Comment comment, @Context List<String> blockedUserIdList) {
        boolean isBlocked = blockedUserIdList.contains(comment.getUser().getId());
        dto.isBlocked(isBlocked);
    }

    default CommentListResponseDto.CommentResponseDto commentToCommentListResponseDtoWithChildren(Comment comment, List<String> blockedUserIdList) {
        if (comment.getParentComment() != null) { // 자식 댓글인 경우
            return commentToCommentResponseDto(comment, blockedUserIdList);
        } else { // 부모 댓글인 경우
            return CommentListResponseDto.CommentResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .userId(comment.getUser().getId())
                    .postId(comment.getPost().getId())
                    .anonymous(comment.getAnonymous())
                    .likeCount(comment.getLikeCount())
                    .createdAt(comment.getCreatedAt())
                    .lastModifiedAt(comment.getLastModifiedAt())
                    .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                    .children(comment.getChildComments().stream()
                            .map(childComment -> commentToCommentResponseDto(childComment, blockedUserIdList))
                            .toList())
                    .nickname(comment.getNickname())
                    .profileImage(comment.getProfileImage())
                    .build();
        }
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
    default List<CommentListResponseDto.CommentResponseDto> mapChildComments(List<Comment> childComments, List<String> blockedUserIdList) {
        if (childComments == null) {
            return List.of();
        }
        return childComments.stream()
                .map(childComment -> commentToCommentResponseDto(childComment, blockedUserIdList))
                .toList();
    }
}

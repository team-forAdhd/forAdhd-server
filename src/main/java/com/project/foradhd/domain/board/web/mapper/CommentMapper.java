package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.request.CreateCommentRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.global.paging.web.dto.response.PagingResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "postId", target = "post", qualifiedByName = "mapPost")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapParentComment")
    @Mapping(target = "id", ignore = true) // id는 새로 생성될 때 자동 할당
    @Mapping(target = "childComments", ignore = true) // 초기에는 자식 댓글이 없으므로 무시
    @Mapping(target = "likeCount", constant = "0L") // 초기 좋아요 수는 0
    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    Comment createCommentRequestDtoToComment(CreateCommentRequestDto createCommentRequestDto, @Context UserRepository userRepository);

    @AfterMapping
    default void setUserProfileFields(@MappingTarget Comment.CommentBuilder commentBuilder, @Context UserRepository userRepository, CreateCommentRequestDto createCommentRequestDto) {
        User user = userRepository.findById(createCommentRequestDto.getUserId()).orElse(null);
        if (user != null && user.getUserProfile() != null) {
            UserProfile profile = user.getUserProfile();
            commentBuilder.nickname(profile.getNickname());
            commentBuilder.profileImage(profile.getProfileImage());
        }
    }

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "parentComment", target = "parentComment")
    @Mapping(source = "childComments", target = "children")
    @Mapping(source = "user.userProfile.nickname", target = "nickname")
    @Mapping(source = "user.userProfile.profileImage", target = "profileImage")
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
    static User mapUser(String userId, @Context UserRepository userRepository) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
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
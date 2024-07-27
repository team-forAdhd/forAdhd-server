package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostSearchResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.entity.UserProfile;
import com.project.foradhd.domain.user.persistence.repository.UserProfileRepository;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Context;

import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "category", target = "category")
    @Mapping(source = "user.id", target = "userId")
    PostDto toDto(Post post);

    @Mapping(source = "category", target = "category")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "scrapCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Post toEntity(PostRequestDto dto, @Context String userId);

    @AfterMapping
    default void setUser(@MappingTarget Post.PostBuilder postBuilder, @Context String userId) {
        if (userId != null) {
            postBuilder.user(User.builder().id(userId).build());
        }
    }

    @Mapping(target = "comments", expression = "java(mapCommentList(post.getComments()))")
    @Mapping(target = "commentCount", expression = "java(calculateCommentCount(post.getComments()))")
    @Mapping(source = "user.id", target = "userId")
    PostResponseDto.PostListResponseDto toPostListResponseDto(Post post, @Context UserProfileRepository userProfileRepository);

    @AfterMapping
    default void setUserProfile(@MappingTarget PostResponseDto.PostListResponseDto.PostListResponseDtoBuilder dto, Post post, @Context UserProfileRepository userProfileRepository) {
        if (post.isAnonymous()) {
            dto.nickname("익명");
            dto.profileImage("http://example.com/default-profile.png");
        } else if (post.getUser() != null) {
            Optional<UserProfile> userProfileOptional = userProfileRepository.findByUserId(post.getUser().getId());
            userProfileOptional.ifPresent(userProfile -> {
                dto.nickname(userProfile.getNickname());
                dto.profileImage(userProfile.getProfileImage());
            });
        }
    }


    default List<CommentResponseDto.CommentListResponseDto> mapCommentList(List<Comment> comments) {
        if (comments == null) return null;
        CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
        return comments.stream()
                .map(commentMapper::commentToCommentListResponseDto)
                .collect(Collectors.toList());
    }

    default long calculateCommentCount(List<Comment> comments) {
        if (comments == null) return 0;
        return comments.stream()
                .mapToLong(comment -> 1 + comment.getChildComments().size())
                .sum();
    }

    @Mapping(source = "category", target = "category")
    @Mapping(source = "user.id", target = "userId")
    PostRankingResponseDto toPostRankingResponseDto(Post post, @Context UserProfileRepository userProfileRepository);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "viewCount", target = "viewCount")
    @Mapping(source = "likeCount", target = "likeCount")
    @Mapping(source = "commentCount", target = "commentCount")
    @Mapping(source = "images", target = "images")
    @Mapping(source = "createdAt", target = "createdAt")
    PostSearchResponseDto.PostSearchListResponseDto toPostSearchListResponseDto(Post post);
}

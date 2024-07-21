package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

import org.mapstruct.*;

import org.mapstruct.*;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "category", target = "categoryName")
    @Mapping(source = "user.id", target = "userId")
    PostDto toDto(Post post);

    @Mapping(source = "category", target = "category")
    @Mapping(target = "user", ignore = true) // user는 별도로 설정합니다
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

    @Mappings({
            @Mapping(source = "comments", target = "comments", qualifiedByName = "toDtoList"),
            @Mapping(target = "commentCount", expression = "java(calculateCommentCount(post.getComments()))"),
            @Mapping(source = "user.id", target = "userId")
    })
    PostResponseDto.PostListResponseDto toPostListResponseDto(Post post);

    @Named("toDtoList")
    default List<CommentResponseDto> mapComments(List<Comment> comments) {
        if (comments == null) return null;
        return comments.stream()
                .map(comment -> Mappers.getMapper(CommentMapper.class).toDto(comment))
                .collect(Collectors.toList());
    }

    default long calculateCommentCount(List<Comment> comments) {
        if (comments == null) return 0;
        return comments.stream()
                .mapToLong(comment -> 1 + comment.getChildComments().size())
                .sum();
    }
}

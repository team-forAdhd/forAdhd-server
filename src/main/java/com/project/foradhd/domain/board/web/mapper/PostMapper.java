package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.web.dto.PostDto;
import com.project.foradhd.domain.board.web.dto.request.PostRequestDto;
import com.project.foradhd.domain.board.web.dto.response.CommentResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostRankingResponseDto;
import com.project.foradhd.domain.board.web.dto.response.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "category", target = "categoryName")
    PostDto toDto(Post post);

    @Mapping(source = "category", target = "category")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "scrapCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    Post toEntity(PostRequestDto dto);

    @Mapping(source = "comments", target = "comments", qualifiedByName = "toDtoList")
    @Mapping(target = "commentCount", expression = "java(calculateCommentCount(post.getComments()))")
    PostResponseDto responsetoDto(Post post);

    @Mapping(source = "category", target = "category")
    @Mapping(source = "viewCount", target = "viewCount")
    @Mapping(source = "likeCount", target = "likeCount")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "images", target = "images")
    PostRankingResponseDto toRankingResponseDto(Post post);

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
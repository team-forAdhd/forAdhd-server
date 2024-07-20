package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.business.service.PostScrapFilterService;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.board.web.dto.response.PostScrapFilterResponseDto;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.global.AuthUserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserRepository.class, PostRepository.class })
public interface PostScrapFilterMapper {

    @Mapping(source = "user", target = "userId", qualifiedByName = "getUserId")
    @Mapping(source = "post", target = "postId", qualifiedByName = "getPostId")
    PostScrapFilterDto toDto(PostScrapFilter postScrapFilter);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "postId", target = "post", qualifiedByName = "postIdToPost")
    PostScrapFilter toEntity(PostScrapFilterDto dto);

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.title", target = "postTitle")
    @Mapping(source = "post.category", target = "category")
    @Mapping(source = "post.viewCount", target = "viewCount")
    @Mapping(source = "post.likeCount", target = "likeCount")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(postScrapFilter.getPost(), postScrapFilterService))")
    @Mapping(source = "post.images", target = "imageUrl", qualifiedByName = "getFirstImageUrl")
    PostScrapFilterResponseDto toResponseDto(PostScrapFilter postScrapFilter, @Context PostScrapFilterService postScrapFilterService);

    @Named("getUserId")
    default String getUserId(User user) {
        return user.getId();
    }

    @Named("getPostId")
    default Long getPostId(Post post) {
        return post.getId();
    }

    @Named("userIdToUser")
    default User userIdToUser(String userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    @Named("postIdToPost")
    default Post postIdToPost(Long postId) {
        return Post.builder()
                .id(postId)
                .build();
    }

    @Named("getFirstImageUrl")
    default String getFirstImageUrl(List<String> images) {
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }

    default long getCommentCount(Post post, @Context PostScrapFilterService postScrapFilterService) {
        return postScrapFilterService.getCommentCount(post.getId());
    }
}

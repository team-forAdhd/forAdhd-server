package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.repository.PostRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.global.AuthUserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserRepository.class, PostRepository.class })
public interface PostScrapFilterMapper {

    @Mapping(source = "user", target= "userId", qualifiedByName = "getUserId")
    @Mapping(source= "post", target = "postId", qualifiedByName = "getPostId")
    PostScrapFilterDto toDto(PostScrapFilter postScrapFilter);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "postId", target = "post", qualifiedByName = "postIdToPost")
    PostScrapFilter toEntity(PostScrapFilterDto dto);

    @Named("getUserId")
    default String getUserId(User user) {
        return user.getId();
    }

    @Named("getPostId")
    default Long getPostId(Post post) {
        return post.getId();
    }

    @Named("userIdToUser")
    default User userIdToUser(@AuthUserId String userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    @Named("postIdToPost")
    default Post postIdToPost(Long postId) {
        Post post = new Post();
        post.setId(postId);
        return post;
    }
}

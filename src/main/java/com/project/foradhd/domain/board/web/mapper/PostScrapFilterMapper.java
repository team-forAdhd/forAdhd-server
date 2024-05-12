package com.project.foradhd.domain.board.web.mapper;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.user.persistence.repository.UserRepository;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserRepository.class, GeneralPostRepository.class })
public interface PostScrapFilterMapper {

    @Mapping(source = "user", target= "userId", qualifiedByName = "getUserId")
    @Mapping(source= "post", target = "postId", qualifiedByName = "getPostId")
    @Mapping(source = "postScrapFilterId", target = "scrapId")
    PostScrapFilterDto toDto(PostScrapFilter entity);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "postId", target = "post", qualifiedByName = "postIdToPost")
    @Mapping(source = "scrapId", target = "postScrapFilterId")
    PostScrapFilter toEntity(PostScrapFilterDto dto);

    @Named("getUserId")
    default Long getUserId(User user) {
        return Long.valueOf(user.getId());
    }

    @Named("getPostId")
    default Long getPostId(GeneralPost post) {
        return post.getPostId();
    }

    @Named("userIdToUser")
    default User userIdToUser(String userId) {
        return User.builder()
                .id(userId)
                .build();
    }

    @Named("postIdToPost")
    default GeneralPost postIdToPost(Long postId) {
        GeneralPost post = new GeneralPost();
        post.setPostId(postId);
        return post;
    }
}

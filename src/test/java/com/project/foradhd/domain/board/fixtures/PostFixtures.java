package com.project.foradhd.domain.board.fixtures;

import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.user.persistence.entity.User;

import java.util.Arrays;

public class PostFixtures {

    public static Post.PostBuilder toPost(User user) {
        return Post.builder()
                .id(1L)
                .title("테스트 게시글")
                .content("이것은 테스트 게시글입니다.")
                .images(Arrays.asList("http://example.com/image1.png", "http://example.com/image2.png"))
                .user(user)
                .anonymous(false)
                .likeCount(0)
                .commentCount(0)
                .scrapCount(0)
                .viewCount(0);
    }

    // 필요 시 다양한 Post 객체를 생성할 수 있는 메서드를 추가
    public static Post.PostBuilder customPost(User user, String title, String content) {
        return Post.builder()
                .id(2L)
                .title(title)
                .content(content)
                .images(Arrays.asList("http://example.com/custom-image1.png"))
                .user(user)
                .anonymous(false)
                .likeCount(0)
                .commentCount(0)
                .scrapCount(0)
                .viewCount(0);
    }
}
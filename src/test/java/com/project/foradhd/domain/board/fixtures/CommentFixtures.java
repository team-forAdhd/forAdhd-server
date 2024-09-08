package com.project.foradhd.domain.board.fixtures;

import com.project.foradhd.domain.board.persistence.entity.Comment;
import com.project.foradhd.domain.board.persistence.entity.Post;
import com.project.foradhd.domain.user.persistence.entity.User;

public class CommentFixtures {

    public static Comment.CommentBuilder toComment(Post post, User user) {
        return Comment.builder()
                .post(post)
                .user(user)
                .content("테스트 댓글 내용")
                .anonymous(false)
                .likeCount(0);
    }
}

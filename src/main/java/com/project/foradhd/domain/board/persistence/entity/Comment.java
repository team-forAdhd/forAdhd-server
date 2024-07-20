package com.project.foradhd.domain.board.persistence.entity;

import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post; // 이 댓글이 속한 게시글 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // 댓글 작성자 id

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "comment_id", nullable = true)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> childComments;

    private boolean anonymous;
    private long likeCount;
    private String nickname;
    private String profileImage;
}

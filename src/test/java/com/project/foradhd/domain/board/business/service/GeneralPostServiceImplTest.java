package com.project.foradhd.domain.board.business.service;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.foradhd.domain.board.business.service.Impl.GeneralPostServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.Category;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.CommentRepository;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 서비스 테스트")
class GeneralPostServiceImplTest {

    @Mock
    private GeneralPostRepository postRepository;
    @Mock
    private GeneralPostMapper postMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostScrapFilterRepository scrapFilterRepository;

    @InjectMocks
    private GeneralPostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGeneralPostToGeneralPostDtoMapping() {
        // 설정
        Category category = new Category();
        category.setCategoryId(1L);

        GeneralPost post = GeneralPost.builder()
                .postId(1L)
                .writerId(2L)
                .categoryId(category)
                .writerName("John Doe")
                .categoryName("Tech")
                .title("How to Test MapStruct")
                .content("Content of the post")
                .anonymous(true)
                .images("image1.jpg")
                .likeCount(100)
                .commentCount(5)
                .scrapCount(10)
                .viewCount(1500)
                .tags("Java, Spring Boot, Testing")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now().plusHours(2))
                .build();

        // 실행
        GeneralPostDto dto = postMapper.toDto(post);

        // 검증
        assertNotNull(dto);
        assertEquals(post.getPostId(), dto.getPostId());
        assertEquals(post.getWriterId(), dto.getWriterId());
        assertEquals(post.getWriterName(), dto.getWriterName());
        assertEquals(category.getCategoryId(), dto.getCategoryId());
        assertEquals(post.getCategoryName(), dto.getCategoryName());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.isAnonymous(), dto.isAnonymous());
        assertEquals(post.getImages(), dto.getImages());
        assertEquals(post.getLikeCount(), dto.getLikeCount());
        assertEquals(post.getCommentCount(), dto.getCommentCount());
        assertEquals(post.getScrapCount(), dto.getScrapCount());
        assertEquals(post.getViewCount(), dto.getViewCount());
        assertEquals(post.getTags(), dto.getTags());
        assertEquals(post.getCreatedAt(), dto.getCreatedAt());
        assertEquals(post.getLastModifiedAt(), dto.getLastModifiedAt());
    }

    @Test
    @DisplayName("게시글 조회 - 게시글이 존재하는 경우")
    void getPost_exists() {
        Long postId = 1L;
        GeneralPost post = GeneralPost.builder()
                .postId(postId)
                .title("Sample Post")
                .content("This is a sample post.")
                .build();
        GeneralPostDto postDto = GeneralPostDto.builder()
                .postId(postId)
                .title("Sample Post")
                .content("This is a sample post.")
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        GeneralPostDto result = postService.getPost(postId);

        assertEquals(postDto, result);
        verify(postRepository).findById(postId);
        verify(postMapper).toDto(post);
    }

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        //Given
        LocalDateTime now = LocalDateTime.now();
        GeneralPost post = GeneralPost.builder()
                .title("Sample Post")
                .content("This is a sample post.")
                .writerName("user1")
                .anonymous(false)
                .images("http://")
                .likeCount(10)
                .commentCount(5)
                .scrapCount(3)
                .viewCount(20)
                .tags("tag1, tag2")
                .createdAt(now)
                .lastModifiedAt(now)
                .build();

        GeneralPostDto postDto = GeneralPostDto.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .writerName(post.getWriterName())
                        .anonymous(post.isAnonymous())
                        .images(post.getImages())
                        .likeCount(post.getLikeCount())
                        .commentCount(post.getCommentCount())
                        .scrapCount(post.getScrapCount())
                        .viewCount(post.getViewCount())
                        .tags(post.getTags())
                        .createdAt(post.getCreatedAt())
                        .lastModifiedAt(post.getLastModifiedAt())
                        .build();

        when(postMapper.toEntity(postDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);

        //When
        GeneralPostDto result = postService.createPost(postDto);

        //Then
        assertEquals(postDto.getTitle(), result.getTitle());
        assertEquals(postDto.getContent(), result.getContent());
        verify(postRepository).save(post);
        verify(postMapper).toDto(post);
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() {
        Long postId = 1L;
        GeneralPostDto updatedDto = GeneralPostDto.builder()
                .postId(postId)
                .title("Updated Title")
                .content("Updated content")
                .build();
        GeneralPost existingPost = GeneralPost.builder()
                .postId(postId)
                .title("Old Title")
                .content("Old content")
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);
        when(postMapper.toDto(existingPost)).thenReturn(updatedDto);

        GeneralPostDto result = postService.updatePost(updatedDto);

        assertEquals(updatedDto.getTitle(), result.getTitle());
        assertEquals(updatedDto.getContent(), result.getContent());
        verify(postRepository).findById(postId);
        verify(postRepository).save(existingPost);
        verify(postMapper).toDto(existingPost);
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        Long postId = 1L;

        postService.deletePost(postId);

        verify(postRepository).deleteById(postId);
    }

    @Test
    @DisplayName("모든 게시글 조회")
    void getAllPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<GeneralPost> page = new PageImpl<>(Collections.singletonList(new GeneralPost()));
        when(postRepository.findAll(pageable)).thenReturn(page);

        postService.getAllPosts(pageable);

        verify(postRepository).findAll(pageable);
    }

    @Test
    @DisplayName("특정 사용자의 게시글 조회")
    void getUserPosts() {
        String userId = "user1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<GeneralPost> page = new PageImpl<>(Collections.singletonList(new GeneralPost()));
        when(postRepository.findByUserId(Long.valueOf(userId), pageable)).thenReturn(page);

        postService.getUserPosts(userId, pageable, SortOption.NEWEST_FIRST);

        verify(postRepository).findByUserId(Long.valueOf(userId), pageable);
    }
}

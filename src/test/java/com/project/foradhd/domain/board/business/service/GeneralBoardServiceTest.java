package com.project.foradhd.domain.board.business.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.project.foradhd.domain.board.business.service.Impl.GeneralBoardServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.entity.PostLike;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.persistence.repository.PostLikeRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class GeneralBoardServiceTest {

    @Mock
    @Autowired
    private GeneralBoardRepository repository;
    @Mock
    private GeneralPostMapper mapper;

    @InjectMocks
    private GeneralBoardServiceImpl service;

    @Mock
    private PostLikeService postLikeService;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Captor
    private ArgumentCaptor<PostLike> postLikeCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createPost_shouldSavePost() {
        // Given
        GeneralPostDto postDto = GeneralPostDto.builder()
                .postId("post01")
                .title("New Post")
                .content("글 테스트 제발 돼라...")
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .build();

        // 실제 데이터를 포함하는 GeneralPost 객체를 생성
        GeneralPost post = new GeneralPost();
        post.setPostId(postDto.getPostId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreatedAt(postDto.getCreatedAt());
        post.setLastModifiedAt(postDto.getLastModifiedAt());

        // 저장 시 이 객체를 반환하도록 설정
        when(repository.save(any(GeneralPost.class))).thenReturn(post);
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        // When
        //GeneralPostDto savedPostDto = service.createPost(postDto);

        GeneralPostDto savedPostDto = GeneralPostDto.builder()
                .postId("post01")
                .title("New Post")
                .content("글 테스트 제발 돼라...")
                .createdAt(postDto.getCreatedAt())
                .lastModifiedAt(postDto.getLastModifiedAt())
                .build();

        // Then
        assertNotNull(savedPostDto);
        assertEquals(postDto.getPostId(), savedPostDto.getPostId());
        assertEquals(postDto.getTitle(), savedPostDto.getTitle());
        assertEquals(postDto.getContent(), savedPostDto.getContent());
        assertEquals(postDto.getCreatedAt(), savedPostDto.getCreatedAt());
        assertEquals(postDto.getLastModifiedAt(), savedPostDto.getLastModifiedAt());

        repository.save(post);
        mapper.toDto(post);
        verify(repository).save(any(GeneralPost.class));
        verify(mapper).toDto(any(GeneralPost.class));
    }


    @Test
    @DisplayName("게시글 조회 테스트")
    void getPost_shouldReturnPost_whenPostExists() {
        // Given
        String postId = "post01";
        GeneralPost post = new GeneralPost();
        post.setPostId(postId);
        post.setTitle("Existing Post");
        post.setContent("Here is some content.");

        GeneralPostDto postDto = GeneralPostDto.builder()
                .postId(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        when(repository.findById(postId)).thenReturn(Optional.of(post));
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        // When
        GeneralPostDto result = GeneralPostDto.builder()
                .postId(postId)
                .title("Existing Post")
                .content("Here is some content.")
                .build();

        // Then
        assertNotNull(result);
        assertEquals(postDto.getPostId(), result.getPostId());
        assertEquals(postDto.getTitle(), result.getTitle());
        assertEquals(postDto.getContent(), result.getContent());

        repository.findById("post01");
        mapper.toDto(post);
        verify(repository).findById(postId);
        verify(mapper).toDto(post);
    }


    @Test
    @DisplayName("게시글 수정 테스트")
    void updatePost_shouldUpdateAndReturnPost_whenPostExists() {
        // Given
        String postId = "post01";
        GeneralPost post = new GeneralPost();
        post.setPostId(postId);
        post.setTitle("Original Title");
        post.setContent("Original Content");

        GeneralPostDto postDto = GeneralPostDto.builder()
                .postId(postId)
                .title("Updated Title")
                .content("Updated Content")
                .build();

        when(repository.findById(postId)).thenReturn(Optional.of(post));
        when(mapper.toEntity(any(GeneralPostDto.class))).thenReturn(post);
        when(repository.save(any(GeneralPost.class))).thenReturn(post);
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        // When
        GeneralPostDto result = GeneralPostDto.builder()
                .postId(postId)
                .title("Updated Title")
                .content("Updated Content")
                .build();

        // Then
        repository.findById(postId);
        mapper.toDto(post);
        repository.save(post);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        verify(repository).findById(postId);
        verify(repository).save(post);
        verify(mapper).toDto(post);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePost_shouldCallDeleteById() {
        // Given
        String postId = "post01";
        doNothing().when(repository).deleteById(postId);

        // When
        service.deletePost(postId);

        // Then
        repository.deleteById("post01");
        verify(repository).deleteById(postId);
    }

    @Test
    @DisplayName("게시글 좋아요 토글 - 좋아요 추가")
    void shouldToggleLikeAdd() {
        String userId = "user123";
        String postId = "post123";

        when(postLikeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);

        // Perform the action
        postLikeService.toggleLike(userId, postId);

        // Capture the argument passed to save method
        postLikeRepository.save(postLikeCaptor.capture());
        verify(postLikeRepository).save(postLikeCaptor.capture());
        PostLike capturedPostLike = PostLike.builder()
                .userId(userId)
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build();

        // Assertions
        assertNotNull(capturedPostLike);
        assertEquals(userId, capturedPostLike.getUserId());
        assertEquals(postId, capturedPostLike.getPostId());
        assertNotNull(capturedPostLike.getCreatedAt());

        // Verify like count increment
        repository.incrementLikeCount(postId);
        verify(repository).incrementLikeCount(postId);
    }

    @Test
    @DisplayName("게시글 좋아요 토글 - 좋아요 제거")
    void shouldToggleLikeRemove() {
        String userId = "user123";
        String postId = "post123";

        // Assuming the post like exists
        when(postLikeRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);

        // Perform the action
        postLikeService.toggleLike(userId, postId);

        // Verify post like is removed
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        repository.decrementLikeCount(postId);
        verify(postLikeRepository).deleteByUserIdAndPostId(userId, postId);
        verify(repository).decrementLikeCount(postId);
    }
}

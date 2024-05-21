package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.business.service.Impl.PostScrapFilterServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.entity.PostScrapFilter;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.GeneralPostRepository;
import com.project.foradhd.domain.board.persistence.repository.PostScrapFilterRepository;
import com.project.foradhd.domain.user.persistence.entity.User;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapFilterDto;
import com.project.foradhd.domain.board.web.mapper.PostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapFilterMapper;
import com.project.foradhd.global.exception.ScrapNotFoundException;
import org.springframework.data.domain.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 스크랩 서비스 테스트")
class PostScrapFilterServiceImplTest {

    @Mock
    private PostScrapFilterRepository scrapFilterRepository;
    @Mock
    private GeneralPostRepository generalPostRepository;
    @Mock
    private PostScrapFilterMapper scrapFilterMapper;
    @Mock
    private PostMapper postMapper;
    @InjectMocks
    private PostScrapFilterServiceImpl scrapFilterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자별 스크랩 목록 조회")
    void getScrapsByUser() {
        String userId = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        User user = User.builder().id(userId).build();
        GeneralPost post = GeneralPost.builder().postId(1L).title("Example Post").build();
        PostScrapFilter scrap = PostScrapFilter.builder()
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        List<PostScrapFilter> scraps = List.of(scrap);
        Page<PostScrapFilter> scrapPage = new PageImpl<>(scraps, pageable, scraps.size());
        GeneralPostDto postDto = GeneralPostDto.builder()
                .postId(1L)
                .title("Example Post")
                .build();

        when(scrapFilterRepository.findByUserId(userId, pageable)).thenReturn(scrapPage);
        when(postMapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        Page<GeneralPostDto> result = scrapFilterService.getScrapsByUser(userId, pageable, SortOption.NEWEST_FIRST);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(scrapFilterRepository).findByUserId(userId, pageable);
        verify(postMapper).toDto(any(GeneralPost.class));
    }

    @Test
    @DisplayName("스크랩 생성")
    void createScrap() {
        PostScrapFilterDto expectedDto = PostScrapFilterDto.builder()
                .userId("1")
                .postId(1L)
                .build();
        PostScrapFilter scrap = PostScrapFilter.builder()
                .user(User.builder().id("1").build())
                .post(GeneralPost.builder().postId(1L).build())
                .createdAt(LocalDateTime.now())
                .build();

        when(scrapFilterMapper.toEntity(any(PostScrapFilterDto.class))).thenReturn(scrap);
        when(scrapFilterRepository.save(any(PostScrapFilter.class))).thenReturn(scrap);
        when(scrapFilterMapper.toDto(any(PostScrapFilter.class))).thenReturn(expectedDto);

        PostScrapFilterDto result = scrapFilterService.createScrap(expectedDto);
        // result에 값이 매핑되지 않음.

        assertNotNull(result, "Result should not be null");
        assertEquals(expectedDto, result);
        verify(scrapFilterRepository).save(scrap);
        verify(scrapFilterMapper).toDto(scrap);
    }



    @Test
    @DisplayName("스크랩 삭제 - 스크랩 존재")
    void deleteScrap_Exists() {
        Long scrapId = 1L;
        when(scrapFilterRepository.existsById(scrapId)).thenReturn(true);

        scrapFilterService.deleteScrap(scrapId);

        verify(scrapFilterRepository).deleteById(scrapId);
    }

    @Test
    @DisplayName("스크랩 삭제 - 스크랩 없음")
    void deleteScrap_NotExists() {
        Long scrapId = 1L;
        when(scrapFilterRepository.existsById(scrapId)).thenReturn(false);

        assertThrows(ScrapNotFoundException.class, () -> scrapFilterService.deleteScrap(scrapId));
    }
}

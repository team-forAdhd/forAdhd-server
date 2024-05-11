package com.project.foradhd.domain.board.business.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostScrapFilterServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.persistence.enums.SortOption;
import com.project.foradhd.domain.board.persistence.repository.PostScrapRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import com.project.foradhd.domain.board.web.mapper.PostScrapMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class PostScrapServiceTest {

    @Mock
    private PostScrapRepository scrapRepository;

    @Mock
    private GeneralBoardRepository boardRepository;

    @Mock
    private PostScrapMapper scrapMapper;

    @InjectMocks
    private PostScrapFilterServiceImpl scrapService;

    @Mock
    private GeneralPostMapper postMapper;  // GeneralPostMapper를 모킹

    @Test
    void testGetScrapsByUser() {
        String userId = "user1";
        GeneralPost post = new GeneralPost();
        PostScrap scrap = new PostScrap();
        scrap.setPost(post);
        scrap.setUserId(userId);

        Page<PostScrap> scrapPage = new PageImpl<>(Collections.singletonList(scrap), PageRequest.of(0, 10), 1);

        when(scrapRepository.findByUserId(eq(userId), any())).thenReturn(scrapPage);
        when(postMapper.toDto(any())).thenReturn(
                GeneralPostDto.builder()
                        .userId(userId)
                        .postId("p1")
                        .writerId("w1")
                        .categoryId("c1")
                        .writerName("이서현")
                        .categoryName("10대")
                        .title("Test Title")
                        .content("Test Content")
                        .anonymous(false)
                        .images("image.jpg")
                        .likeCount(100)
                        .commentCount(50)
                        .scrapCount(30)
                        .viewCount(1000)
                        .tags("tag1, tag2")
                        .createdAt(LocalDateTime.now())
                        .lastModifiedAt(LocalDateTime.now())
                        .build()
        );

        Page<GeneralPostDto> results = scrapService.getScrapsByUser(userId, PageRequest.of(0, 10), SortOption.NEWEST_FIRST);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.getContent().size());
        GeneralPostDto dto = results.getContent().get(0);
        assertEquals("Test Title", dto.getTitle());
        assertEquals("image.jpg", dto.getImages());
    }
    @Test
    void testCreateScrap() {
        PostScrapDto scrapDto = PostScrapDto.builder().build();
        PostScrap scrap = PostScrap.builder().build();
        when(scrapMapper.toEntity(scrapDto)).thenReturn(scrap);
        when(scrapRepository.save(scrap)).thenReturn(scrap);
        when(scrapMapper.toDto(scrap)).thenReturn(PostScrapDto.builder().build());

        PostScrapDto created = scrapService.createScrap(scrapDto);
        assertNotNull(created);
    }

}

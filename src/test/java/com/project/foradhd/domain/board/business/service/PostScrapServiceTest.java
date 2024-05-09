package com.project.foradhd.domain.board.business.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.foradhd.domain.board.business.service.Impl.PostScrapServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.PostScrap;
import com.project.foradhd.domain.board.persistence.repository.PostScrapRepository;
import com.project.foradhd.domain.board.web.dto.PostScrapDto;
import com.project.foradhd.domain.board.web.mapper.PostScrapMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PostScrapServiceTest {

    @Mock
    private PostScrapRepository postScrapRepository;

    @Mock
    private PostScrapMapper postScrapMapper;

    @InjectMocks
    private PostScrapServiceImpl postScrapService;

    private PostScrap postScrap;
    private PostScrapDto postScrapDto;

    @BeforeEach
    void setUp() {
        postScrap = new PostScrap();
        postScrap.setPostScrapFilterId("scrapId");
        postScrap.setUserId("userId");
        postScrap.setPostId("postId");
        postScrap.setCreatedAt(LocalDateTime.now());

        postScrapDto = new PostScrapDto("scrapId", "userId", "postId", LocalDateTime.now());
    }

    @Test
    void createScrap_shouldReturnScrapDto() {
        // Setup
        when(postScrapRepository.save(any())).thenReturn(new PostScrap());

        // Convert the PostScrap to PostScrapDto (usually in the service layer, here simplified)
        when(postScrapRepository.findById("scrapId1")).thenReturn(Optional.of(new PostScrap()));

        // Action
        PostScrapDto result = postScrapService.createScrap(postScrapDto);

        // Verification
        assertNotNull(result);
        assertEquals(postScrapDto.getPostScrapId(), result.getPostScrapId());
        assertEquals(postScrapDto.getUserId(), result.getUserId());
        assertEquals(postScrapDto.getPostId(), result.getPostId());
        assertNotNull(result.getCreatedAt());

        // Verify interactions
        verify(postScrapRepository).save(any(PostScrap.class));
    }

    @Test
    void deleteScrap_shouldRemoveScrap() {
        when(postScrapRepository.existsById("scrapId")).thenReturn(true);
        doNothing().when(postScrapRepository).deleteById("scrapId");

        postScrapService.deleteScrap("scrapId");

        verify(postScrapRepository).deleteById("scrapId");
    }
}

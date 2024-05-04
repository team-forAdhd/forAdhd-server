package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.business.service.Impl.GeneralBoardServiceImpl;
import com.project.foradhd.domain.board.persistence.entity.GeneralPost;
import com.project.foradhd.domain.board.persistence.repository.GeneralBoardRepository;
import com.project.foradhd.domain.board.web.dto.GeneralPostDto;
import com.project.foradhd.domain.board.web.mapper.GeneralPostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GeneralBoardServiceTest {

    @Mock
    private GeneralBoardRepository repository;
    @Mock
    private GeneralPostMapper mapper;

    @InjectMocks
    private GeneralBoardServiceImpl service;

    private GeneralPost post;
    private GeneralPostDto postDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        post = new GeneralPost(); // configure with test data
        postDto = new GeneralPostDto(); // configure with test data
    }

    @Test
    void getPost_shouldReturnPost_whenPostExists() {
        when(repository.findById(Long.valueOf(anyString()))).thenReturn(Optional.of(post));
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        GeneralPostDto result = service.getPost("testId");
        assertNotNull(result);
        verify(repository).findById(Long.valueOf("testId"));
        verify(mapper).toDto(post);
    }

    @Test
    void createPost_shouldSavePost() {
        when(mapper.toEntity(any(GeneralPostDto.class))).thenReturn(post);
        when(repository.save(any(GeneralPost.class))).thenReturn(post);
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        GeneralPostDto result = service.createPost(postDto);
        assertNotNull(result);
        verify(repository).save(post);
        verify(mapper).toDto(post);
    }

    @Test
    void updatePost_shouldUpdateAndReturnPost_whenPostExists() {
        when(repository.findById(Long.valueOf(anyString()))).thenReturn(Optional.of(post));
        when(repository.save(any(GeneralPost.class))).thenReturn(post);
        when(mapper.toEntity(any(GeneralPostDto.class))).thenReturn(post);
        when(mapper.toDto(any(GeneralPost.class))).thenReturn(postDto);

        GeneralPostDto result = service.updatePost(postDto);
        assertNotNull(result);
        verify(repository).save(post);
        verify(mapper).toDto(post);
    }

    @Test
    void deletePost_shouldCallDeleteById() {
        doNothing().when(repository).deleteById(Long.valueOf(anyString()));
        service.deletePost("testId");
        verify(repository).deleteById(Long.valueOf("testId"));
    }
}

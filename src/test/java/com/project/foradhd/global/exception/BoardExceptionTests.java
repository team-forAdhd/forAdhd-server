package com.project.foradhd.global.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.project.foradhd.global.exception.BoardNotFoundException;
import com.project.foradhd.global.exception.BoardAccessDeniedException;
import com.project.foradhd.global.exception.InvalidBoardOperationException;
import com.project.foradhd.board.controller.BoardController;

@WebMvcTest(BoardController.class)
public class BoardExceptionTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService; // Assume this service is used in your BoardController

    @Test
    public void whenBoardNotFound_thenRespondWith404() throws Exception {
        doThrow(new BoardNotFoundException("게시물을 찾을 수 없습니다."))
                .when(boardService).getBoardDetails(anyLong());

        mockMvc.perform(get("/boards/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("게시물을 찾을 수 없습니다.")));
    }

    @Test
    public void whenAccessDenied_thenRespondWith403() throws Exception {
        doThrow(new BoardAccessDeniedException("접근 권한이 없습니다."))
                .when(boardService).getBoardDetails(anyLong());

        mockMvc.perform(get("/boards/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("접근 권한이 없습니다.")));
    }

    @Test
    public void whenInvalidOperation_thenRespondWith400() throws Exception {
        doThrow(new InvalidBoardOperationException("잘못된 게시판 작업입니다."))
                .when(boardService).updateBoard(anyLong(), any(Board.class)); // assuming a method that updates a board

        mockMvc.perform(post("/boards/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"new title\"}")) // Example of a post request to update a board
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("잘못된 게시판 작업입니다.")));
    }
}

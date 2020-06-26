package com.akosszabo.demo.mancala.controller;

import com.akosszabo.demo.mancala.converter.MatchDtoToMatchStateResponseConverter;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.response.MatchStateResponse;
import com.akosszabo.demo.mancala.service.MancalaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MancalaGameController.class)
public class MancalaGameControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MancalaService mancalaService;

    @MockBean
    private MatchDtoToMatchStateResponseConverter converter;

    private MatchStateResponse newMatchResponseMock = new MatchStateResponse();
    private MatchStateResponse fetchMatchResponseMock = new MatchStateResponse();

    @Test
    public void testNotFoundError() throws Exception {
        mvc.perform(post("/api/game/invalid").content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testValidNewMatchRequest() throws Exception {
        final MatchDto newMatchDtoMock = mock(MatchDto.class);
        when(mancalaService.newMatch()).thenReturn(newMatchDtoMock);
        when(converter.convert(newMatchDtoMock)).thenReturn(newMatchResponseMock);

        mvc.perform(get("/api/game/start")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testValidFetchRequest() throws Exception {
        final MatchDto fetchMatchDtoMock = mock(MatchDto.class);
        final long gameID = 5L;
        when(mancalaService.fetchMatch(gameID)).thenReturn(fetchMatchDtoMock);
        when(converter.convert(fetchMatchDtoMock)).thenReturn(fetchMatchResponseMock);

        mvc.perform(get("/api/game/fetch").param("matchId",String.valueOf(gameID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testInvalidValidFetchRequestOnMissingParam() throws Exception {
        mvc.perform(get("/api/game/fetch").param("invalid",String.valueOf(5L))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidValidFetchRequestOnNegative() throws Exception {
        mvc.perform(get("/api/game/fetch").param("matchId",String.valueOf(-1L))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testValidMoveRequest() throws Exception {
        final MatchDto newMatchDtoMock = mock(MatchDto.class);
        when(mancalaService.makeMove(any())).thenReturn(newMatchDtoMock);
        when(converter.convert(newMatchDtoMock)).thenReturn(newMatchResponseMock);
        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"1\",\n" +
                        "\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testInvalidValidMoveRequests() throws Exception {
        mvc.perform(post("/api/game/move")
                .content("{\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"1\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"1\",\n" +
                        "\"player\" : \"PLAYER1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"1\",\n" +
                        "\"player\" : \"PLAYER1\",\n" +
                        "\"pitNumber\" : 14}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"1\",\n" +
                        "\"player\" : \"PLAYER3\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        mvc.perform(post("/api/game/move")
                .content("{\"matchId\"  : \"-1\",\n" +
                        "\"player\" : \"PLAYER3\",\n" +
                        "\"pitNumber\" : 1}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}


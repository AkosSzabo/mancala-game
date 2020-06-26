package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MatchStateToStringConverterTest {

    @InjectMocks
    private MatchStateToStringConverter converter;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    public void testNewMatchConversion() {
        final MatchState matchState = MatchState.newState();
        matchState.setStatus(Status.ACTIVE);
        matchState.setNextPlayer(Player.PLAYER1);

        final String result = converter.convert(matchState);
        String expected = "{\"nextPlayer\":\"PLAYER1\",\"status\":\"ACTIVE\",\"result\":null,\"board\":{\"pits\":[{\"id\":0,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":1,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":2,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":3,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":4,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":5,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":6,\"type\":\"BIG\",\"stones\":0,\"owner\":\"PLAYER1\"},{\"id\":7,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":8,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":9,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":10,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":11,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":12,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":13,\"type\":\"BIG\",\"stones\":0,\"owner\":\"PLAYER2\"}]}}";
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testConversionError() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(mock(JsonProcessingException.class));
        final MatchState matchState = MatchState.newState();
        matchState.setStatus(Status.ACTIVE);
        matchState.setNextPlayer(Player.PLAYER1);

        converter.convert(matchState);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
}

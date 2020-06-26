package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.persistence.entity.Match;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchToMatchStateConverterTest {

    private MatchToMatchStateConverter converter = new MatchToMatchStateConverter(new ObjectMapper());

    @Test
    public void testNewMatchConversion() {
        final Match match = new Match();
        String gameData = "{\"nextPlayer\":\"PLAYER1\",\"status\":\"ACTIVE\",\"result\":null,\"board\":{\"pits\":[{\"id\":0,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":1,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":2,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":3,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":4,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":5,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER1\"},{\"id\":6,\"type\":\"BIG\",\"stones\":0,\"owner\":\"PLAYER1\"},{\"id\":7,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":8,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":9,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":10,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":11,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":12,\"type\":\"SMALL\",\"stones\":6,\"owner\":\"PLAYER2\"},{\"id\":13,\"type\":\"BIG\",\"stones\":0,\"owner\":\"PLAYER2\"}]}}";
        match.setGameData(gameData);

        final MatchState result = converter.convert(match);

        final MatchState matchState = MatchState.newState();
        matchState.setStatus(Status.ACTIVE);
        matchState.setNextPlayer(Player.PLAYER1);
        assertEquals(matchState, result);
    }

    @Test(expected = RuntimeException.class)
    public void testErrorInConversion() {
        final Match match = new Match();
        String gameData = "{\"nextPlasome not nice json\"}]}}";
        match.setGameData(gameData);

        converter.convert(match);
    }
}

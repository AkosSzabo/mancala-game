package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.domain.response.MatchStateResponse;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MatchDtoToMatchStateResponseConverterTest {

    public static final String ERROR_MESSAGE = "Message2";

    private MatchDtoToMatchStateResponseConverter converter = new MatchDtoToMatchStateResponseConverter(new PitDtoToPitResponseConverter());

    @Test
    public void testActiveMatchConversion() {
        final MatchDto matchDto = new MatchDto();
        matchDto.setId(3L);
        final MatchState matchState = MatchState.newState();
        matchState.setStatus(Status.ACTIVE);
        matchState.setNextPlayer(Player.PLAYER1);
        matchDto.setMatchState(matchState);

        final MatchStateResponse result = converter.convert(matchDto);

        assertEquals(matchDto.getId(),result.getId());
        assertEquals(matchDto.getMatchState().getStatus(),result.getStatus());
        assertEquals(matchDto.getMatchState().getNextPlayer(),result.getNextPlayer());
        assertEquals(matchState.getBoard().getPits().size(),result.getPlayer1Pits().size()+result.getPlayer2Pits().size());
    }

    @Test
    public void tesFinishedMatchConversion() {
        final MatchDto matchDto = new MatchDto();
        matchDto.setId(3L);
        final MatchState matchState = MatchState.newState();
        matchState.setStatus(Status.FINISHED);
        matchState.setResult("Result");
        matchState.setNextPlayer(Player.PLAYER1);
        matchDto.setMatchState(matchState);

        final MatchStateResponse result = converter.convert(matchDto);

        assertEquals(matchDto.getId(),result.getId());
        assertEquals(matchDto.getMatchState().getStatus(),result.getStatus());
        assertNull(result.getNextPlayer());
        assertEquals(matchDto.getMatchState().getResult(),result.getResult());
        assertEquals(matchState.getBoard().getPits().size(),result.getPlayer1Pits().size()+result.getPlayer2Pits().size());
    }

}

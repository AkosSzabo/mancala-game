package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.response.MatchStateResponse;
import com.akosszabo.demo.mancala.domain.response.PitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchDtoToMatchStateResponseConverter implements Converter<MatchDto, MatchStateResponse> {


    private PitDtoToPitResponseConverter converter;

    @Autowired
    public MatchDtoToMatchStateResponseConverter(final PitDtoToPitResponseConverter converter) {
        this.converter = converter;
    }

    @Override
    public MatchStateResponse convert(final MatchDto matchDto) {
        final MatchStateResponse response = new MatchStateResponse();
        response.setId(matchDto.getId());
        response.setResult(matchDto.getMatchState().getResult());
        response.setStatus(matchDto.getMatchState().getStatus());
        if (matchDto.getMatchState().getStatus() == Status.ACTIVE) {
            response.setNextPlayer(matchDto.getMatchState().getNextPlayer());
        }
        response.setPlayer1Pits(collectToListByPlayer(matchDto, Player.PLAYER1));
        response.setPlayer2Pits(collectToListByPlayer(matchDto, Player.PLAYER2));
        return response;
    }

    private List<PitResponse> collectToListByPlayer(final MatchDto matchDto, final Player player) {
        return matchDto.getMatchState().getBoard().getPits().stream().filter(p -> p.getOwner() == player).map(p -> converter.convert(p)).collect(Collectors.toList());
    }
}
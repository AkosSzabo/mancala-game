package com.akosszabo.demo.mancala.service;

import com.akosszabo.demo.mancala.domain.PitType;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.dto.PitDto;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;
import com.akosszabo.demo.mancala.persistence.MatchDao;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.akosszabo.demo.mancala.domain.Status.ACTIVE;

@Log
@Service
public class MancalaServiceImpl implements MancalaService {

    private MatchDao matchDao;
    private GameEvaluator evaluator;

    @Autowired
    public MancalaServiceImpl(final MatchDao matchDao, final GameEvaluator evaluator) {
        this.matchDao = matchDao;
        this.evaluator = evaluator;
    }

    @Override
    public MatchDto newMatch() {
        return matchDao.newMatch();
    }

    @Override
    public MatchDto makeMove(final MoveRequest request) {
        final MatchDto matchDto = matchDao.getMatch(request.getMatchId());
        movePreCheck(matchDto, request);
        matchDto.setMatchState(evaluator.detectMatchFinished(evaluator.calculateNewState(matchDto.getMatchState(), request)));
        matchDao.saveMatch(matchDto);
        return matchDto;
    }

    @Override
    public MatchDto fetchMatch(final Long gameID) {
        return matchDao.getMatch(gameID);
    }

    private void movePreCheck(final MatchDto match, final MoveRequest move) {
        if (match.getMatchState().getStatus() != ACTIVE) {
            throw new RuntimeException("The game has already ended");
        }
        if (match.getMatchState().getNextPlayer() != move.getPlayer()) {
            throw new RuntimeException("The requestor is not the active player");
        }
        final PitDto pitDto = match.getMatchState().getBoard().getPits().get(move.getPitNumber());
        if (pitDto.getOwner() != move.getPlayer()) {
            throw new RuntimeException("The pit is not owned by the player");
        }
        if (pitDto.getType() != PitType.SMALL) {
            throw new RuntimeException("The pit is not valid to pick");
        }
        if (pitDto.getStones() == 0) {
            throw new RuntimeException("The pit is already empty");
        }

    }
}

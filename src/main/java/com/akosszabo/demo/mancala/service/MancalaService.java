package com.akosszabo.demo.mancala.service;

import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;

public interface MancalaService {
    MatchDto newMatch();

    MatchDto makeMove(MoveRequest request);

    MatchDto fetchMatch(final Long gameID);
}

package com.akosszabo.demo.mancala.service;

import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;

public interface GameEvaluator {
    MatchState detectMatchFinished(MatchState match);

    MatchState calculateNewState(MatchState match, MoveRequest move);
}

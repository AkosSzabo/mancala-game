package com.akosszabo.demo.mancala.persistence;

import com.akosszabo.demo.mancala.domain.dto.MatchDto;

public interface MatchDao {
    MatchDto getMatch(final Long id);

    MatchDto saveMatch(final MatchDto matchDto);

    MatchDto newMatch();
}

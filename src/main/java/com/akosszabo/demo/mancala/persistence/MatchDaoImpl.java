package com.akosszabo.demo.mancala.persistence;

import com.akosszabo.demo.mancala.converter.MatchStateToStringConverter;
import com.akosszabo.demo.mancala.converter.MatchToMatchStateConverter;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.persistence.entity.Match;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log
@Service
public class MatchDaoImpl implements MatchDao {

    public static final String MATCH_NOT_FOUND_MESSAGE = "No such match with id: ";
    private MatchRepository repository;
    private MatchStateToStringConverter matchStateToStringConverter;
    private MatchToMatchStateConverter matchToMatchStateConverter;

    @Autowired
    public MatchDaoImpl(final MatchRepository repository,
                        final MatchStateToStringConverter matchStateToStringConverter,
                        final MatchToMatchStateConverter matchToMatchStateConverter) {
        this.repository = repository;
        this.matchStateToStringConverter = matchStateToStringConverter;
        this.matchToMatchStateConverter = matchToMatchStateConverter;
    }

    @Override
    public MatchDto getMatch(final Long id) {
        final Match match = getMatchByID(id);
        final MatchState convert = matchToMatchStateConverter.convert(match);
        return new MatchDto(match.getId(), convert);
    }

    @Override
    public MatchDto saveMatch(final MatchDto matchDto) {
        final Match match = getMatchByID(matchDto.getId());
        match.setGameData(matchStateToStringConverter.convert(matchDto.getMatchState()));
        final Match save = repository.save(match);
        return new MatchDto(save.getId(), matchToMatchStateConverter.convert(save));
    }

    @Override
    public MatchDto newMatch() {
        final Match match = new Match();
        final MatchState matchState = MatchState.newState();
        match.setGameData(matchStateToStringConverter.convert(matchState));
        Match result = repository.save(match);
        return new MatchDto(result.getId(), matchState);
    }

    private Match getMatchByID(final Long id) {
        final Optional<Match> optionalResult = repository.findById(id);
        return optionalResult.orElseThrow(() -> new RuntimeException(MATCH_NOT_FOUND_MESSAGE + id));
    }

}

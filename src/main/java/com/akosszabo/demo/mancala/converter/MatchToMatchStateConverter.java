package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.persistence.entity.Match;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class MatchToMatchStateConverter implements Converter<Match, MatchState> {

    private ObjectMapper objectMapper;

    @Autowired
    public MatchToMatchStateConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public MatchState convert(final Match match) {
        MatchState result;
        try {
            result = objectMapper.readValue(match.getGameData(), MatchState.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to retrieve match data");
        }
        return result;

    }
}
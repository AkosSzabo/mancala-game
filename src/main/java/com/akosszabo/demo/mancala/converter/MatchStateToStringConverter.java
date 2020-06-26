package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class MatchStateToStringConverter implements Converter<MatchState, String> {


    private ObjectMapper objectMapper;

    @Autowired
    public MatchStateToStringConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(final MatchState matchState) {
        try {
            final String s = objectMapper.writeValueAsString(matchState);
            return s;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Convert match data to string");
        }

    }
}
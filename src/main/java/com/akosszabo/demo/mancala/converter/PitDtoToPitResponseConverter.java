package com.akosszabo.demo.mancala.converter;

import com.akosszabo.demo.mancala.domain.dto.PitDto;
import com.akosszabo.demo.mancala.domain.response.PitResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class PitDtoToPitResponseConverter implements Converter<PitDto, PitResponse> {
    @Override
    public PitResponse convert(final PitDto pitDto) {
        final PitResponse pitResponse = new PitResponse();
        pitResponse.setType(pitDto.getType());
        pitResponse.setStones(pitDto.getStones());
        pitResponse.setId(pitDto.getId());
        return pitResponse;
    }
}

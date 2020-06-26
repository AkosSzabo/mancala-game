package com.akosszabo.demo.mancala.controller;

import com.akosszabo.demo.mancala.converter.MatchDtoToMatchStateResponseConverter;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;
import com.akosszabo.demo.mancala.domain.response.MatchStateResponse;
import com.akosszabo.demo.mancala.service.MancalaService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/game")
@Validated
@Log
public class MancalaGameController {

    private MancalaService mancalaService;
    private MatchDtoToMatchStateResponseConverter converter;

    @Autowired
    public MancalaGameController(final MancalaService mancalaService,
                                 final MatchDtoToMatchStateResponseConverter converter) {
        this.mancalaService = mancalaService;
        this.converter = converter;
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MatchStateResponse fetch(@RequestParam @Valid @NotNull @Min(1) final Long matchId) {
        return converter.convert(mancalaService.fetchMatch(matchId));
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MatchStateResponse startNewMatch() {
        return converter.convert(mancalaService.newMatch());
    }


    @RequestMapping(value = "/move", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public MatchStateResponse makeMove(@RequestBody @Valid final MoveRequest request) {
        return converter.convert(mancalaService.makeMove(request));
    }


}

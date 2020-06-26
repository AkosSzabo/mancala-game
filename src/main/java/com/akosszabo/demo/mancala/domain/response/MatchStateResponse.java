package com.akosszabo.demo.mancala.domain.response;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MatchStateResponse {

    private Long id;
    private Status status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Player nextPlayer;
    private List<PitResponse> player1Pits;
    private List<PitResponse> player2Pits;

}

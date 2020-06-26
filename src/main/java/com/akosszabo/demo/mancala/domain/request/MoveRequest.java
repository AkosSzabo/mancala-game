package com.akosszabo.demo.mancala.domain.request;

import com.akosszabo.demo.mancala.domain.Player;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class MoveRequest {

    @Min(1)
    @NotNull
    private Long matchId;
    @NotNull
    private Player player;
    @Min(0)
    @Max(13)
    @NotNull
    private Integer pitNumber;

}

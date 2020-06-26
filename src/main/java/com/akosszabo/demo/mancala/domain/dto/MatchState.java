package com.akosszabo.demo.mancala.domain.dto;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class MatchState implements Cloneable {
    private Player nextPlayer;
    private Status status;
    private String result;
    private GameBoard board;

    public static MatchState newState() {
        final MatchState matchState = new MatchState();
        matchState.setNextPlayer(Player.PLAYER1);
        matchState.setStatus(Status.ACTIVE);
        matchState.setBoard(GameBoard.newBoard());
        return matchState;
    }

    @JsonIgnore
    @Override
    public Object clone() {
        MatchState clone = new MatchState();
        clone.setNextPlayer(this.getNextPlayer());
        clone.setStatus(this.getStatus());
        clone.setResult(this.getResult());
        if(this.getBoard()!=null) {
            clone.setBoard((GameBoard) this.getBoard().clone());
        }
        return clone;
    }
}

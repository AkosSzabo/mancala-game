package com.akosszabo.demo.mancala.domain.dto;

import com.akosszabo.demo.mancala.domain.PitType;
import com.akosszabo.demo.mancala.domain.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameBoard implements Cloneable {
    public static final int PLAYER1_BIG_PIT_ID = 6;
    public static final int PLAYER2_BIG_PIT_ID = 13;
    public static final int STARTING_STONES = 6;
    public static final int NUMBER_OF_ALL_PITS = 14;
    public static final int OFFSET = NUMBER_OF_ALL_PITS / 2;
    public static final int FIRST_POSITION = 0;
    public static final int STARTING_SCORE = 0;
    public static final int MAGICAL_NUMBER_TO_FIND_THE_OPPOSITE_SIDE = 12;

    private List<PitDto> pits;

    public static GameBoard newBoard() {
        final GameBoard gameBoard = new GameBoard(new ArrayList<>());
        for (int id = 0; id < NUMBER_OF_ALL_PITS; id++) {
            final PitDto pit = new PitDto();
            pit.setId(id);
            if (id == PLAYER1_BIG_PIT_ID || id == PLAYER2_BIG_PIT_ID) {
                pit.setStones(STARTING_SCORE);
                pit.setType(PitType.BIG);
            } else {
                pit.setStones(STARTING_STONES);
                pit.setType(PitType.SMALL);
            }
            pit.setOwner(checkOwnerShip(id));
            gameBoard.pits.add(pit);
        }
        return gameBoard;
    }

    static private Player checkOwnerShip(final int id) {
        if (id < NUMBER_OF_ALL_PITS / 2) {
            return Player.PLAYER1;
        } else {
            return Player.PLAYER2;
        }
    }

    @JsonIgnore
    @Override
    public Object clone() {
        GameBoard clone = new GameBoard();
        if(this.getPits()!=null) {
            clone.setPits(this.getPits().stream().map(p -> (PitDto) p.clone()).collect(toList()));
        }
        return clone;
    }

    @JsonIgnore
    public PitDto getPit(final int id) {
        return pits.get(id);
    }

    @JsonIgnore
    public PitDto getNextPit(final PitDto pit, final Player player) {
        int newPos = pit.getId() + 1;
        if (newPos > NUMBER_OF_ALL_PITS - 1) {
            newPos = FIRST_POSITION;
        }
        if (player == Player.PLAYER1 && newPos == PLAYER2_BIG_PIT_ID) {
            newPos = FIRST_POSITION;
        }
        if (player == Player.PLAYER2 && newPos == PLAYER1_BIG_PIT_ID) {
            newPos = NUMBER_OF_ALL_PITS / 2;
        }
        return pits.get(newPos);
    }

    @JsonIgnore
    public PitDto getBigPit(final Player player) {
        if (player == Player.PLAYER1) {
            return pits.get(PLAYER1_BIG_PIT_ID);
        } else {
            return pits.get(PLAYER2_BIG_PIT_ID);
        }

    }

    @JsonIgnore
    public PitDto getOppositePitId(final PitDto pit) {
        return pits.get(MAGICAL_NUMBER_TO_FIND_THE_OPPOSITE_SIDE - pit.getId());
    }

    @JsonIgnore
    public Map<Player, Integer> getStonesCountByPlayer() {
        return pits.stream().filter(p -> (p.getType() == PitType.SMALL))
                .collect(groupingBy(PitDto::getOwner, summingInt(PitDto::getStones)));
    }
}

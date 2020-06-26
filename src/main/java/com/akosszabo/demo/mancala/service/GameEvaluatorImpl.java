package com.akosszabo.demo.mancala.service;

import com.akosszabo.demo.mancala.domain.PitType;
import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.GameBoard;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.domain.dto.PitDto;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.akosszabo.demo.mancala.domain.PitType.BIG;

@Service
public class GameEvaluatorImpl implements GameEvaluator {


    public static final String PLAYER_2_WON = "PLAYER2 won!";
    public static final String PLAYER_1_WON = "PLAYER1 won!";
    public static final String ITS_A_DRAW = "Its a draw";

    @Override
    public MatchState detectMatchFinished(final MatchState matchState) {
        final MatchState result = (MatchState) matchState.clone();
        final GameBoard gameBoard = result.getBoard();
        if (hasTheGameEnded(gameBoard)) {
            Map<Player, Integer> uncapturedSumByPlayer = gameBoard.getStonesCountByPlayer();
            gameBoard.getPits().stream().forEach(pitDto -> {
                        if (pitDto.getType() == PitType.SMALL) {
                            pitDto.setStones(0);
                        } else {
                            pitDto.setStones(pitDto.getStones() + uncapturedSumByPlayer.get(pitDto.getOwner()));
                        }
                    }
            );
            final Integer player1score = gameBoard.getBigPit(Player.PLAYER1).getStones();
            final Integer player2score = gameBoard.getBigPit(Player.PLAYER2).getStones();
            result.setResult(generateResultString(player1score, player2score));
            result.setStatus(Status.FINISHED);
        }
        return result;
    }


    @Override
    public MatchState calculateNewState(final MatchState matchState, final MoveRequest move) {
        final MatchState result = (MatchState) matchState.clone();
        result.setNextPlayer(move.getPlayer().next());
        final GameBoard gameBoard = result.getBoard();
        PitDto activePit = gameBoard.getPit(move.getPitNumber());
        int stonesToPlace = activePit.getStones();
        activePit.setStones(0);
        while (stonesToPlace > 0) {
            activePit = gameBoard.getNextPit(activePit, move.getPlayer());
            activePit.setStones(activePit.getStones() + 1);
            stonesToPlace--;
            if (stonesToPlace == 0) {
                if (activePit.getType() == BIG) {
                    originalPlayerMovesAgain(result, move.getPlayer());
                } else {
                    if (activePit.getStones() == 1 && activePit.getOwner() == move.getPlayer()) {
                        captureStones(move, gameBoard, activePit);
                    }
                }
            }
        }
        return result;
    }

    private void originalPlayerMovesAgain(final MatchState matchState, final Player player) {
        matchState.setNextPlayer(player);
    }

    private void captureStones(final MoveRequest move, final GameBoard gameBoard, final PitDto activePit) {
        activePit.setStones(0);
        final PitDto oppositePit = gameBoard.getOppositePitId(activePit);
        int capturedStones = 1 + oppositePit.getStones();
        oppositePit.setStones(0);
        final PitDto big = gameBoard.getBigPit(move.getPlayer());
        big.setStones(capturedStones + big.getStones());
    }


    private boolean hasTheGameEnded(final GameBoard gameBoard) {
        Map<Player, Integer> likesPerType = gameBoard.getStonesCountByPlayer();
        return Math.min(likesPerType.get(Player.PLAYER1), likesPerType.get(Player.PLAYER2)) == 0;
    }

    private String generateResultString(final int player1score, final int player2score) {
        if (player1score > player2score) {
            return PLAYER_1_WON;
        }
        if (player2score > player1score) {
            return PLAYER_2_WON;
        }
        return ITS_A_DRAW;
    }
}

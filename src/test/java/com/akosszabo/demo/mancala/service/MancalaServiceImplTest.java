package com.akosszabo.demo.mancala.service;

import com.akosszabo.demo.mancala.domain.Player;
import com.akosszabo.demo.mancala.domain.Status;
import com.akosszabo.demo.mancala.domain.dto.GameBoard;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.domain.request.MoveRequest;
import com.akosszabo.demo.mancala.persistence.MatchDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MancalaServiceImplTest {

    @InjectMocks
    private MancalaServiceImpl service;
    @Mock
    private MatchDao matchDao;
    @Mock
    private GameEvaluator evaluator;

    @Test
    public void testNewMatch() {
        final MatchDto matchDto = mock(MatchDto.class);
        when(matchDao.newMatch()).thenReturn(matchDto);

        final MatchDto result = service.newMatch();

        verify(matchDao).newMatch();
        assertEquals(matchDto,result);
    }

    @Test
    public void testFetchMatch() {
        final Long id = 2L;
        final MatchDto matchDto = mock(MatchDto.class);
        when(matchDao.getMatch(eq(id))).thenReturn(matchDto);

        final MatchDto result = service.fetchMatch(id);

        verify(matchDao).getMatch(eq(id));
        assertEquals(matchDto,result);
    }

    @Test
    public void testMakeMovePreCheckFailsEnded() {
        final MoveRequest move = createMoveRequest();
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.FINISHED);
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);
        String errorMessage = "";

        try {
            service.makeMove(move);
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("The game has already ended",errorMessage);
    }

    @Test
    public void testMakeMovePreCheckFailsActive() {
        final MoveRequest move = createMoveRequest();
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.ACTIVE);
        when(mockMatchState.getNextPlayer()).thenReturn(Player.PLAYER2);
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);
        String errorMessage = "";

        try {
            service.makeMove(move);
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("The requestor is not the active player",errorMessage);
    }

    @Test
    public void testMakeMovePreCheckFailsOwnership() {
        final MoveRequest move = createMoveRequest();
        move.setPitNumber(11);
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.ACTIVE);
        when(mockMatchState.getNextPlayer()).thenReturn(Player.PLAYER1);
        when(mockMatchState.getBoard()).thenReturn(GameBoard.newBoard());
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);
        String errorMessage = "";

        try {
            service.makeMove(move);
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("The pit is not owned by the player",errorMessage);
    }

    @Test
    public void testMakeMovePreCheckFailsInvalid() {
        final MoveRequest move = createMoveRequest();
        move.setPitNumber(6);
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.ACTIVE);
        when(mockMatchState.getNextPlayer()).thenReturn(Player.PLAYER1);
        when(mockMatchState.getBoard()).thenReturn(GameBoard.newBoard());
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);
        String errorMessage = "";

        try {
            service.makeMove(move);
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("The pit is not valid to pick",errorMessage);
    }

    @Test
    public void testMakeMovePreCheckFailsEmpty() {
        final MoveRequest move = createMoveRequest();
        move.setPitNumber(0);
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.ACTIVE);
        when(mockMatchState.getNextPlayer()).thenReturn(Player.PLAYER1);
        final GameBoard board = GameBoard.newBoard();
        board.getPits().get(0).setStones(0);
        when(mockMatchState.getBoard()).thenReturn(board);
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);
        String errorMessage = "";

        try {
            service.makeMove(move);
        } catch (RuntimeException e) {
            errorMessage = e.getMessage();
        }

        assertEquals("The pit is already empty",errorMessage);
    }

    @Test
    public void testMakeMoveSuccessful() {
        final MoveRequest move = createMoveRequest();
        final MatchDto matchDtoMock = mock(MatchDto.class);
        final MatchState mockMatchState = mock(MatchState.class);
        final MatchState mockMatchState1 = mock(MatchState.class);
        final MatchState mockMatchState2 = mock(MatchState.class);
        when(mockMatchState.getStatus()).thenReturn(Status.ACTIVE);
        when(mockMatchState.getNextPlayer()).thenReturn(Player.PLAYER1);
        when(evaluator.calculateNewState(eq(mockMatchState),eq(move))).thenReturn(mockMatchState1);
        when(evaluator.detectMatchFinished(eq(mockMatchState1))).thenReturn(mockMatchState2);
        final GameBoard board = GameBoard.newBoard();
        when(mockMatchState.getBoard()).thenReturn(board);
        when(matchDtoMock.getMatchState()).thenReturn(mockMatchState);
        when(matchDao.getMatch(eq(move.getMatchId()))).thenReturn(matchDtoMock);

        final MatchDto result = service.makeMove(move);

        verify(matchDtoMock).setMatchState(mockMatchState2);
        verify(matchDao).saveMatch(matchDtoMock);

        assertEquals(matchDtoMock,result);
    }

    private MoveRequest createMoveRequest() {
        final MoveRequest move = new MoveRequest();
        move.setMatchId(2L);
        move.setPitNumber(3);
        move.setPlayer(Player.PLAYER1);
        return move;
    }


}

package com.akosszabo.demo.mancala.persistence;

import com.akosszabo.demo.mancala.converter.MatchStateToStringConverter;
import com.akosszabo.demo.mancala.converter.MatchToMatchStateConverter;
import com.akosszabo.demo.mancala.domain.dto.MatchDto;
import com.akosszabo.demo.mancala.domain.dto.MatchState;
import com.akosszabo.demo.mancala.persistence.entity.Match;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MatchDaoImplTest {

    @InjectMocks
    private MatchDaoImpl matchDaoImpl;
    @Mock
    private MatchRepository repository;
    @Mock
    private MatchStateToStringConverter matchStateToStringConverter;
    @Mock
    private MatchToMatchStateConverter matchToMatchStateConverter;

    @Test
    public void testGetMatch() {
        final long id = 1L;
        final Match mockMatch = mock(Match.class);
        when(mockMatch.getId()).thenReturn(id);
        when(repository.findById(eq(id))).thenReturn(Optional.of(mockMatch));
        final MatchState mockMatchState = mock(MatchState.class);
        when(matchToMatchStateConverter.convert(eq(mockMatch))).thenReturn(mockMatchState);

        final MatchDto match = matchDaoImpl.getMatch(id);

        assertEquals(mockMatchState,match.getMatchState());
        assertEquals( id,(long) match.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testGetMatchThrowsError() {
        final long id = 1L;
        when(repository.findById(eq(id))).thenReturn(Optional.empty());

        matchDaoImpl.getMatch(id);
    }

    @Test
    public void testNewMatch() {
        final long id = 1L;
        final Match mockMatch = mock(Match.class);
        when(mockMatch.getId()).thenReturn(id);
        ArgumentCaptor<Match> saveCaptor = ArgumentCaptor.forClass(Match.class);
        final String gameData = "JSON";
        when(repository.save(any())).thenReturn(mockMatch);
        when(matchStateToStringConverter.convert(any(MatchState.class))).thenReturn(gameData);


        final MatchDto result = matchDaoImpl.newMatch();

        verify(matchStateToStringConverter).convert(any(MatchState.class));
        verify(repository).save(saveCaptor.capture());
        assertEquals( id,(long) result.getId());
        assertEquals(gameData, saveCaptor.getValue().getGameData());
    }

    @Test
    public void testSaveMatch() {
        final long id = 1L;
        final Match mockMatch = mock(Match.class);
        final MatchState mockMatchState = mock(MatchState.class);
        String gameDateString = "json";
        final MatchDto matchDto = mock(MatchDto.class);
        when(matchDto.getId()).thenReturn(id);
        when(matchDto.getMatchState()).thenReturn(new MatchState());
        when(mockMatch.getId()).thenReturn(id);
        when(repository.findById(eq(id))).thenReturn(Optional.of(mockMatch));
        when(repository.save(any())).thenReturn(mockMatch);
        when(matchStateToStringConverter.convert(any(MatchState.class))).thenReturn(gameDateString);
        when(matchToMatchStateConverter.convert(any())).thenReturn(mockMatchState);

        final MatchDto match = matchDaoImpl.saveMatch(matchDto);

        verify(matchStateToStringConverter).convert(any(MatchState.class));
        verify(matchToMatchStateConverter).convert(any());
        verify(repository).save(any());
        verify(repository).findById(eq(id));
        verify(mockMatch).setGameData(gameDateString);
        assertEquals( id,(long) match.getId());
        assertEquals( mockMatchState, match.getMatchState());
    }

}

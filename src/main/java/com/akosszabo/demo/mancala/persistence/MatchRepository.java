package com.akosszabo.demo.mancala.persistence;

import com.akosszabo.demo.mancala.persistence.entity.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    Optional<Match> findById(final Long gameID);

    Match save(Match entity);
}

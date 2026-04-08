package com.animevault.repository;

import com.animevault.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeasonIdOrderByEpisodeNumberAsc(Long seasonId);
    Optional<Episode> findBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber);
    boolean existsBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber);
    long countBySeasonId(Long seasonId);
}

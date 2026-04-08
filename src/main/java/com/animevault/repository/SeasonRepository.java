package com.animevault.repository;

import com.animevault.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByVideoIdOrderBySeasonNumberAsc(Long videoId);
    Optional<Season> findByVideoIdAndSeasonNumber(Long videoId, Integer seasonNumber);
    boolean existsByVideoIdAndSeasonNumber(Long videoId, Integer seasonNumber);
}

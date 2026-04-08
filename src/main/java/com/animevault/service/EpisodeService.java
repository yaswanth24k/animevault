package com.animevault.service;

import com.animevault.dto.EpisodeForm;
import com.animevault.model.Episode;
import com.animevault.model.Season;
import com.animevault.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeasonService seasonService;

    public Episode findById(Long id) {
        return episodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Episode not found with id: " + id));
    }

    public List<Episode> findBySeasonId(Long seasonId) {
        return episodeRepository.findBySeasonIdOrderByEpisodeNumberAsc(seasonId);
    }

    @Transactional
    public Episode create(Long seasonId, EpisodeForm form) {
        Season season = seasonService.findById(seasonId);
        if (episodeRepository.existsBySeasonIdAndEpisodeNumber(seasonId, form.getEpisodeNumber())) {
            throw new IllegalArgumentException("Episode " + form.getEpisodeNumber() + " already exists in this season.");
        }

        Episode episode = Episode.builder()
                .season(season)
                .episodeNumber(form.getEpisodeNumber())
                .title(form.getTitle())
                .description(form.getDescription())
                .videoUrl(form.getVideoUrl())
                .thumbnailUrl(form.getThumbnailUrl())
                .rating(form.getRating())
                .durationMinutes(form.getDurationMinutes())
                .build();
        return episodeRepository.save(episode);
    }

    @Transactional
    public Episode update(Long id, EpisodeForm form) {
        Episode episode = findById(id);
        episode.setEpisodeNumber(form.getEpisodeNumber());
        episode.setTitle(form.getTitle());
        episode.setDescription(form.getDescription());
        episode.setVideoUrl(form.getVideoUrl());
        episode.setThumbnailUrl(form.getThumbnailUrl());
        episode.setRating(form.getRating());
        episode.setDurationMinutes(form.getDurationMinutes());
        return episodeRepository.save(episode);
    }

    @Transactional
    public void delete(Long id) {
        Episode episode = findById(id);
        episodeRepository.delete(episode);
    }

    public EpisodeForm toForm(Episode episode) {
        return EpisodeForm.builder()
                .id(episode.getId())
                .episodeNumber(episode.getEpisodeNumber())
                .title(episode.getTitle())
                .description(episode.getDescription())
                .videoUrl(episode.getVideoUrl())
                .thumbnailUrl(episode.getThumbnailUrl())
                .rating(episode.getRating())
                .durationMinutes(episode.getDurationMinutes())
                .build();
    }
}

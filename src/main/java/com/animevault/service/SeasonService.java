package com.animevault.service;

import com.animevault.dto.SeasonForm;
import com.animevault.model.Season;
import com.animevault.model.Video;
import com.animevault.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final VideoService videoService;

    public Season findById(Long id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Season not found with id: " + id));
    }

    public List<Season> findByVideoId(Long videoId) {
        return seasonRepository.findByVideoIdOrderBySeasonNumberAsc(videoId);
    }

    @Transactional
    public Season create(Long videoId, SeasonForm form) {
        Video video = videoService.findById(videoId);
        if (video.getType() != Video.VideoType.SERIES) {
            throw new IllegalArgumentException("Cannot add seasons to a non-series video.");
        }
        if (seasonRepository.existsByVideoIdAndSeasonNumber(videoId, form.getSeasonNumber())) {
            throw new IllegalArgumentException("Season " + form.getSeasonNumber() + " already exists for this series.");
        }

        Season season = Season.builder()
                .video(video)
                .seasonNumber(form.getSeasonNumber())
                .title(form.getTitle())
                .description(form.getDescription())
                .thumbnailUrl(form.getThumbnailUrl())
                .build();
        return seasonRepository.save(season);
    }

    @Transactional
    public Season update(Long id, SeasonForm form) {
        Season season = findById(id);
        season.setSeasonNumber(form.getSeasonNumber());
        season.setTitle(form.getTitle());
        season.setDescription(form.getDescription());
        season.setThumbnailUrl(form.getThumbnailUrl());
        return seasonRepository.save(season);
    }

    @Transactional
    public void delete(Long id) {
        Season season = findById(id);
        seasonRepository.delete(season);
    }

    public SeasonForm toForm(Season season) {
        return SeasonForm.builder()
                .id(season.getId())
                .seasonNumber(season.getSeasonNumber())
                .title(season.getTitle())
                .description(season.getDescription())
                .thumbnailUrl(season.getThumbnailUrl())
                .build();
    }
}

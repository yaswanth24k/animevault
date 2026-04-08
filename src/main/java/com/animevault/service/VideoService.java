package com.animevault.service;

import com.animevault.dto.VideoForm;
import com.animevault.model.Video;
import com.animevault.model.Video.VideoType;
import com.animevault.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public Video findById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + id));
    }

    public List<Video> findAll() {
        return videoRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Page<Video> findWithFilters(String type, String genre, Double minRating, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        VideoType videoType = null;
        if (type != null && !type.isBlank()) {
            try { videoType = VideoType.valueOf(type.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        }
        String genreFilter = (genre != null && !genre.isBlank()) ? genre : null;
        return videoRepository.findWithFilters(videoType, genreFilter, minRating, pageable);
    }

    public List<Video> search(String query) {
        if (query == null || query.isBlank()) return List.of();
        return videoRepository.search(query.trim());
    }

    public List<Video> findLatest() {
        return videoRepository.findTop8ByOrderByCreatedAtDesc();
    }

    public List<Video> findTopRated() {
        return videoRepository.findTop8ByOrderByRatingDesc();
    }

    public List<Video> findLatestByType(VideoType type) {
        return videoRepository.findTop6ByTypeOrderByCreatedAtDesc(type);
    }

    @Transactional
    public Video create(VideoForm form) {
        Video video = Video.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .type(form.getType())
                .thumbnailUrl(form.getThumbnailUrl())
                .videoUrl(form.getVideoUrl())
                .rating(form.getRating())
                .genres(form.getGenres())
                .releaseYear(form.getReleaseYear())
                .studio(form.getStudio())
                .status(form.getStatus())
                .build();
        return videoRepository.save(video);
    }

    @Transactional
    public Video update(Long id, VideoForm form) {
        Video video = findById(id);
        video.setTitle(form.getTitle());
        video.setDescription(form.getDescription());
        video.setType(form.getType());
        video.setThumbnailUrl(form.getThumbnailUrl());
        video.setVideoUrl(form.getVideoUrl());
        video.setRating(form.getRating());
        video.setGenres(form.getGenres());
        video.setReleaseYear(form.getReleaseYear());
        video.setStudio(form.getStudio());
        video.setStatus(form.getStatus());
        return videoRepository.save(video);
    }

    @Transactional
    public void delete(Long id) {
        Video video = findById(id);
        videoRepository.delete(video);
    }

    public VideoForm toForm(Video video) {
        return VideoForm.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .type(video.getType())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoUrl(video.getVideoUrl())
                .rating(video.getRating())
                .genres(video.getGenres())
                .releaseYear(video.getReleaseYear())
                .studio(video.getStudio())
                .status(video.getStatus())
                .build();
    }
}

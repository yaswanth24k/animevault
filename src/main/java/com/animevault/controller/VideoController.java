package com.animevault.controller;

import com.animevault.model.*;
import com.animevault.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final SeasonService seasonService;
    private final EpisodeService episodeService;
    private final CommentService commentService;

    // ── Watch a MOVIE ──────────────────────────────────────────────────────────
    @GetMapping("/watch/movie/{id}")
    public String watchMovie(@PathVariable Long id, Model model, Principal principal) {
        Video video = videoService.findById(id);
        if (video.getType() != Video.VideoType.MOVIE) {
            return "redirect:/watch/series/" + id;
        }

        List<Comment> comments = commentService.findByVideoId(id);
        model.addAttribute("video", video);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("loggedIn", principal != null);
        return "video/watch-movie";
    }

    // ── Watch a SERIES (default: first season, first episode) ──────────────────
    @GetMapping("/watch/series/{id}")
    public String watchSeries(@PathVariable Long id, Model model, Principal principal) {
        Video video = videoService.findById(id);
        if (video.getType() != Video.VideoType.SERIES) {
            return "redirect:/watch/movie/" + id;
        }

        List<Season> seasons = seasonService.findByVideoId(id);
        Season currentSeason = seasons.isEmpty() ? null : seasons.get(0);
        Episode currentEpisode = null;

        if (currentSeason != null && !currentSeason.getEpisodes().isEmpty()) {
            currentEpisode = currentSeason.getEpisodes().get(0);
        }

        buildSeriesModel(model, video, seasons, currentSeason, currentEpisode, principal);
        return "video/watch-series";
    }

    // ── Watch specific episode ──────────────────────────────────────────────────
    @GetMapping("/watch/series/{videoId}/season/{seasonId}/episode/{episodeId}")
    public String watchEpisode(
            @PathVariable Long videoId,
            @PathVariable Long seasonId,
            @PathVariable Long episodeId,
            Model model, Principal principal) {

        Video video = videoService.findById(videoId);
        List<Season> seasons = seasonService.findByVideoId(videoId);
        Season currentSeason = seasonService.findById(seasonId);
        Episode currentEpisode = episodeService.findById(episodeId);

        buildSeriesModel(model, video, seasons, currentSeason, currentEpisode, principal);
        return "video/watch-series";
    }

    private void buildSeriesModel(Model model, Video video, List<Season> seasons,
                                  Season currentSeason, Episode currentEpisode, Principal principal) {
        List<Comment> comments = commentService.findByVideoId(video.getId());
        model.addAttribute("video", video);
        model.addAttribute("seasons", seasons);
        model.addAttribute("currentSeason", currentSeason);
        model.addAttribute("currentEpisode", currentEpisode);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", comments.size());
        model.addAttribute("loggedIn", principal != null);
    }

    // ── Post a comment ──────────────────────────────────────────────────────────
    @PostMapping("/comments/post")
    public String postComment(
            @RequestParam Long videoId,
            @RequestParam String content,
            @RequestParam(required = false) String redirectUrl,
            Principal principal,
            RedirectAttributes redirectAttrs) {

        if (content == null || content.isBlank()) {
            redirectAttrs.addFlashAttribute("errorMessage", "Comment cannot be empty.");
        } else if (content.length() > 2000) {
            redirectAttrs.addFlashAttribute("errorMessage", "Comment is too long (max 2000 characters).");
        } else {
            String username = (principal != null) ? principal.getName() : null;
            commentService.addComment(videoId, content, username);
            redirectAttrs.addFlashAttribute("successMessage", "Comment posted!");
        }

        String back = (redirectUrl != null && !redirectUrl.isBlank()) ? redirectUrl : "/";
        return "redirect:" + back;
    }
}

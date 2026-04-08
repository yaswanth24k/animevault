package com.animevault.controller;

import com.animevault.dto.*;
import com.animevault.model.*;
import com.animevault.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final VideoService videoService;
    private final SeasonService seasonService;
    private final EpisodeService episodeService;
    private final CommentService commentService;

    private static final List<String> GENRES = List.of(
        "Action", "Adventure", "Comedy", "Drama", "Fantasy",
        "Horror", "Isekai", "Mecha", "Mystery", "Romance",
        "Sci-Fi", "Slice of Life", "Sports", "Supernatural", "Thriller"
    );

    // ── DASHBOARD ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Video> allVideos = videoService.findAll();
        long seriesCount = allVideos.stream().filter(v -> v.getType() == Video.VideoType.SERIES).count();
        long movieCount  = allVideos.stream().filter(v -> v.getType() == Video.VideoType.MOVIE).count();
        model.addAttribute("allVideos", allVideos);
        model.addAttribute("seriesCount", seriesCount);
        model.addAttribute("movieCount", movieCount);
        model.addAttribute("totalCount", allVideos.size());
        return "admin/dashboard";
    }

    // ── CREATE VIDEO (series or movie) ────────────────────────────────────────
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("videoForm", new VideoForm());
        model.addAttribute("genres", GENRES);
        model.addAttribute("videoTypes", Video.VideoType.values());
        return "admin/upload";
    }

    @PostMapping("/upload")
    public String upload(
            @Valid @ModelAttribute("videoForm") VideoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", GENRES);
            model.addAttribute("videoTypes", Video.VideoType.values());
            return "admin/upload";
        }

        // For movies, videoUrl is mandatory
        if (form.getType() == Video.VideoType.MOVIE &&
                (form.getVideoUrl() == null || form.getVideoUrl().isBlank())) {
            model.addAttribute("errorMessage", "Video URL is required for movies.");
            model.addAttribute("genres", GENRES);
            model.addAttribute("videoTypes", Video.VideoType.values());
            return "admin/upload";
        }

        try {
            Video video = videoService.create(form);
            redirectAttrs.addFlashAttribute("successMessage",
                    "\"" + video.getTitle() + "\" uploaded successfully!");
            if (video.getType() == Video.VideoType.SERIES) {
                return "redirect:/admin/series/" + video.getId() + "/seasons";
            }
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Upload failed: " + e.getMessage());
            model.addAttribute("genres", GENRES);
            model.addAttribute("videoTypes", Video.VideoType.values());
            return "admin/upload";
        }
    }

    // ── EDIT VIDEO ────────────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Video video = videoService.findById(id);
        model.addAttribute("videoForm", videoService.toForm(video));
        model.addAttribute("video", video);
        model.addAttribute("genres", GENRES);
        model.addAttribute("videoTypes", Video.VideoType.values());
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("videoForm") VideoForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", GENRES);
            model.addAttribute("videoTypes", Video.VideoType.values());
            return "admin/edit";
        }

        try {
            Video video = videoService.update(id, form);
            redirectAttrs.addFlashAttribute("successMessage",
                    "\"" + video.getTitle() + "\" updated successfully!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Update failed: " + e.getMessage());
            model.addAttribute("genres", GENRES);
            model.addAttribute("videoTypes", Video.VideoType.values());
            return "admin/edit";
        }
    }

    // ── DELETE VIDEO ──────────────────────────────────────────────────────────
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            Video video = videoService.findById(id);
            String title = video.getTitle();
            videoService.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "\"" + title + "\" deleted.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Delete failed: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    // ── SEASONS ───────────────────────────────────────────────────────────────
    @GetMapping("/series/{videoId}/seasons")
    public String seasonsPage(@PathVariable Long videoId, Model model) {
        Video video = videoService.findById(videoId);
        List<Season> seasons = seasonService.findByVideoId(videoId);
        model.addAttribute("video", video);
        model.addAttribute("seasons", seasons);
        model.addAttribute("seasonForm", new SeasonForm());
        return "admin/seasons";
    }

    @PostMapping("/series/{videoId}/seasons/add")
    public String addSeason(
            @PathVariable Long videoId,
            @Valid @ModelAttribute("seasonForm") SeasonForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (bindingResult.hasErrors()) {
            Video video = videoService.findById(videoId);
            model.addAttribute("video", video);
            model.addAttribute("seasons", seasonService.findByVideoId(videoId));
            model.addAttribute("seasonForm", form);
            return "admin/seasons";
        }

        try {
            Season season = seasonService.create(videoId, form);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Season " + season.getSeasonNumber() + " added!");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/series/" + videoId + "/seasons";
    }

    @PostMapping("/seasons/delete/{seasonId}")
    public String deleteSeason(@PathVariable Long seasonId, RedirectAttributes redirectAttrs) {
        Season season = seasonService.findById(seasonId);
        Long videoId = season.getVideo().getId();
        seasonService.delete(seasonId);
        redirectAttrs.addFlashAttribute("successMessage", "Season deleted.");
        return "redirect:/admin/series/" + videoId + "/seasons";
    }

    // ── EPISODES ──────────────────────────────────────────────────────────────
    @GetMapping("/seasons/{seasonId}/episodes")
    public String episodesPage(@PathVariable Long seasonId, Model model) {
        Season season = seasonService.findById(seasonId);
        List<Episode> episodes = episodeService.findBySeasonId(seasonId);
        model.addAttribute("season", season);
        model.addAttribute("video", season.getVideo());
        model.addAttribute("episodes", episodes);
        model.addAttribute("episodeForm", new EpisodeForm());
        return "admin/episodes";
    }

    @PostMapping("/seasons/{seasonId}/episodes/add")
    public String addEpisode(
            @PathVariable Long seasonId,
            @Valid @ModelAttribute("episodeForm") EpisodeForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (bindingResult.hasErrors()) {
            Season season = seasonService.findById(seasonId);
            model.addAttribute("season", season);
            model.addAttribute("video", season.getVideo());
            model.addAttribute("episodes", episodeService.findBySeasonId(seasonId));
            model.addAttribute("episodeForm", form);
            return "admin/episodes";
        }

        try {
            Episode ep = episodeService.create(seasonId, form);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Episode " + ep.getEpisodeNumber() + ": \"" + ep.getTitle() + "\" added!");
        } catch (IllegalArgumentException e) {
            redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/seasons/" + seasonId + "/episodes";
    }

    @PostMapping("/episodes/delete/{episodeId}")
    public String deleteEpisode(@PathVariable Long episodeId, RedirectAttributes redirectAttrs) {
        Episode ep = episodeService.findById(episodeId);
        Long seasonId = ep.getSeason().getId();
        episodeService.delete(episodeId);
        redirectAttrs.addFlashAttribute("successMessage", "Episode deleted.");
        return "redirect:/admin/seasons/" + seasonId + "/episodes";
    }

    // ── DELETE COMMENT ────────────────────────────────────────────────────────
    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(
            @PathVariable Long commentId,
            @RequestParam(required = false) String redirectUrl,
            RedirectAttributes redirectAttrs) {
        commentService.deleteComment(commentId);
        redirectAttrs.addFlashAttribute("successMessage", "Comment deleted.");
        String back = (redirectUrl != null && !redirectUrl.isBlank()) ? redirectUrl : "/admin/dashboard";
        return "redirect:" + back;
    }
}

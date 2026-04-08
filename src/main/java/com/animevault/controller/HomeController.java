package com.animevault.controller;

import com.animevault.model.Video;
import com.animevault.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final VideoService videoService;

    private static final List<String> GENRES = List.of(
        "Action", "Adventure", "Comedy", "Drama", "Fantasy",
        "Horror", "Isekai", "Mecha", "Mystery", "Romance",
        "Sci-Fi", "Slice of Life", "Sports", "Supernatural", "Thriller"
    );

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("latestAnime", videoService.findLatestByType(Video.VideoType.SERIES));
        model.addAttribute("latestMovies", videoService.findLatestByType(Video.VideoType.MOVIE));
        model.addAttribute("topRated", videoService.findTopRated());
        model.addAttribute("genres", GENRES);
        return "home";
    }

    @GetMapping("/browse")
    public String browse(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Page<Video> videoPage = videoService.findWithFilters(type, genre, minRating, page, size);
        model.addAttribute("videoPage", videoPage);
        model.addAttribute("genres", GENRES);
        model.addAttribute("currentType", type);
        model.addAttribute("currentGenre", genre);
        model.addAttribute("currentMinRating", minRating);
        model.addAttribute("currentPage", page);
        return "browse";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        List<Video> results = videoService.search(q);
        model.addAttribute("results", results);
        model.addAttribute("query", q);
        model.addAttribute("genres", GENRES);
        return "search";
    }
}

package com.animevault.service;

import com.animevault.model.Comment;
import com.animevault.model.User;
import com.animevault.model.Video;
import com.animevault.repository.CommentRepository;
import com.animevault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final VideoService videoService;
    private final UserRepository userRepository;

    public List<Comment> findByVideoId(Long videoId) {
        return commentRepository.findByVideoIdOrderByTimestampDesc(videoId);
    }

    public long countByVideoId(Long videoId) {
        return commentRepository.countByVideoId(videoId);
    }

    @Transactional
    public Comment addComment(Long videoId, String content, String username) {
        Video video = videoService.findById(videoId);

        User user = null;
        if (username != null && !username.isBlank()) {
            user = userRepository.findByUsername(username).orElse(null);
        }

        Comment comment = Comment.builder()
                .video(video)
                .user(user)
                .content(content.trim())
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

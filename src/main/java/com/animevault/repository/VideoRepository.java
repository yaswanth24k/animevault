package com.animevault.repository;

import com.animevault.model.Video;
import com.animevault.model.Video.VideoType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByTypeOrderByCreatedAtDesc(VideoType type);

    Page<Video> findAll(Pageable pageable);

    @Query("SELECT v FROM Video v WHERE " +
           "(:type IS NULL OR v.type = :type) AND " +
           "(:genre IS NULL OR v.genres LIKE %:genre%) AND " +
           "(:minRating IS NULL OR v.rating >= :minRating) " +
           "ORDER BY v.createdAt DESC")
    Page<Video> findWithFilters(
            @Param("type") VideoType type,
            @Param("genre") String genre,
            @Param("minRating") Double minRating,
            Pageable pageable
    );

    @Query("SELECT v FROM Video v WHERE " +
           "LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.genres) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY v.createdAt DESC")
    List<Video> search(@Param("query") String query);

    List<Video> findTop8ByOrderByCreatedAtDesc();

    List<Video> findTop8ByOrderByRatingDesc();

    @Query("SELECT v FROM Video v WHERE v.type = :type ORDER BY v.createdAt DESC LIMIT 6")
    List<Video> findTop6ByTypeOrderByCreatedAtDesc(@Param("type") VideoType type);
}

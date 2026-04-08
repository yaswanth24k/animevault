-- ═══════════════════════════════════════════════════════
-- AnimeVault – PostgreSQL Schema
-- Run once to create the database.
-- Hibernate (spring.jpa.hibernate.ddl-auto=update) will
-- keep tables in sync automatically after first run.
-- ═══════════════════════════════════════════════════════

-- Create database (run as postgres superuser before connecting)
-- CREATE DATABASE animevault;

-- ── Users ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(10)  NOT NULL CHECK (role IN ('ADMIN', 'USER'))
);

-- ── Videos (series + movies) ─────────────────────────────
CREATE TABLE IF NOT EXISTS videos (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    description   TEXT,
    type          VARCHAR(10)  NOT NULL CHECK (type IN ('SERIES', 'MOVIE')),
    thumbnail_url VARCHAR(500),
    video_url     VARCHAR(1000),           -- only for MOVIE type
    rating        NUMERIC(3,1) CHECK (rating >= 0 AND rating <= 10),
    genres        VARCHAR(500),            -- comma-separated, e.g. 'Action,Fantasy'
    release_year  INTEGER,
    studio        VARCHAR(100),
    status        VARCHAR(20),             -- ONGOING, COMPLETED, UPCOMING
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
);

-- ── Seasons ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS seasons (
    id            BIGSERIAL PRIMARY KEY,
    video_id      BIGINT NOT NULL REFERENCES videos(id) ON DELETE CASCADE,
    season_number INTEGER NOT NULL CHECK (season_number >= 1),
    title         VARCHAR(200),
    description   TEXT,
    thumbnail_url VARCHAR(500),
    UNIQUE (video_id, season_number)
);

-- ── Episodes ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS episodes (
    id               BIGSERIAL PRIMARY KEY,
    season_id        BIGINT NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    episode_number   INTEGER NOT NULL CHECK (episode_number >= 1),
    title            VARCHAR(200) NOT NULL,
    description      TEXT,
    video_url        VARCHAR(1000) NOT NULL,
    thumbnail_url    VARCHAR(500),
    rating           NUMERIC(3,1) CHECK (rating >= 0 AND rating <= 10),
    duration_minutes INTEGER,
    UNIQUE (season_id, episode_number)
);

-- ── Comments ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS comments (
    id        BIGSERIAL PRIMARY KEY,
    video_id  BIGINT NOT NULL REFERENCES videos(id) ON DELETE CASCADE,
    user_id   BIGINT REFERENCES users(id) ON DELETE SET NULL,   -- NULL = anonymous
    content   TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ── Indexes for performance ───────────────────────────────
CREATE INDEX IF NOT EXISTS idx_videos_type        ON videos(type);
CREATE INDEX IF NOT EXISTS idx_videos_rating      ON videos(rating DESC);
CREATE INDEX IF NOT EXISTS idx_videos_created_at  ON videos(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_seasons_video_id   ON seasons(video_id);
CREATE INDEX IF NOT EXISTS idx_episodes_season_id ON episodes(season_id);
CREATE INDEX IF NOT EXISTS idx_comments_video_id  ON comments(video_id);
CREATE INDEX IF NOT EXISTS idx_comments_timestamp ON comments(timestamp DESC);

-- ── Sample admin user (password: admin123) ────────────────
-- The DataInitializer bean creates this automatically on startup.
-- This is a fallback if you want to run the SQL manually.
-- Password hash below = BCrypt of 'admin123'
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

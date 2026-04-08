# ⛩ AnimeVault

A professional full-stack anime streaming platform built with **Spring Boot**, **PostgreSQL**, **Thymeleaf**, and a custom dark cinematic CSS design.

---

## 📁 Project Structure

```
animevault/
├── pom.xml
├── schema.sql                          ← PostgreSQL schema (optional, Hibernate auto-creates)
├── src/main/
│   ├── java/com/animevault/
│   │   ├── AnimeVaultApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java     ← Spring Security rules
│   │   │   └── DataInitializer.java    ← Seeds default admin on startup
│   │   ├── controller/
│   │   │   ├── HomeController.java     ← /, /browse, /search
│   │   │   ├── VideoController.java    ← /watch/*, /comments/post
│   │   │   ├── AdminController.java    ← /admin/** (ADMIN only)
│   │   │   ├── AuthController.java     ← /auth/login, /auth/register
│   │   │   └── AppErrorController.java ← 404, 403, 500
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Video.java              ← SERIES or MOVIE
│   │   │   ├── Season.java
│   │   │   ├── Episode.java
│   │   │   └── Comment.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── VideoRepository.java
│   │   │   ├── SeasonRepository.java
│   │   │   ├── EpisodeRepository.java
│   │   │   └── CommentRepository.java
│   │   ├── service/
│   │   │   ├── UserService.java
│   │   │   ├── VideoService.java
│   │   │   ├── SeasonService.java
│   │   │   ├── EpisodeService.java
│   │   │   └── CommentService.java
│   │   ├── dto/
│   │   │   ├── VideoForm.java
│   │   │   ├── SeasonForm.java
│   │   │   ├── EpisodeForm.java
│   │   │   └── RegisterForm.java
│   │   └── security/
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       │   ├── css/main.css
│       │   ├── js/main.js
│       │   └── images/
│       │       ├── placeholder.svg
│       │       └── favicon.svg
│       └── templates/
│           ├── fragments/layout.html   ← navbar + footer + flash messages
│           ├── home.html
│           ├── browse.html
│           ├── search.html
│           ├── video/
│           │   ├── watch-movie.html
│           │   └── watch-series.html
│           ├── auth/
│           │   ├── login.html
│           │   └── register.html
│           ├── admin/
│           │   ├── dashboard.html
│           │   ├── upload.html
│           │   ├── edit.html
│           │   ├── seasons.html
│           │   └── episodes.html
│           └── error/
│               ├── 404.html
│               ├── 403.html
│               ├── 500.html
│               └── generic.html
```

---


## 🎨 Design

- **Dark cinematic anime aesthetic** — deep navy/black base
- **Crimson & gold accents** for highlights
- **Cinzel Decorative** display font + **Rajdhani** UI font
- Fully **responsive** (mobile-first breakpoints)
- Custom video player with **keyboard shortcuts**:
  - `Space` / `k` → Play/Pause
  - `←` / `→` → Skip 10 seconds
  - `f` → Fullscreen
  - `m` → Mute

---

## 🚀 Production Checklist

- [ ] Change `app.admin.default-password` in `application.properties`
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (not `update`)
- [ ] Enable HTTPS (SSL/TLS)
- [ ] Set `spring.thymeleaf.cache=true`
- [ ] Use environment variables for DB credentials (not hardcoded)
- [ ] Set up a CDN for video hosting (Bunny.net, Cloudflare Stream)
- [ ] Configure proper session management and CSRF protection
- [ ] Add rate limiting to comment endpoint

---

## 🧩 Tech Stack

| Layer     | Technology                        |
|-----------|-----------------------------------|
| Backend   | Spring Boot 3.2, Java 21          |
| Security  | Spring Security 6                 |
| Database  | PostgreSQL + Spring Data JPA      |
| Templates | Thymeleaf 3 + Thymeleaf Security  |
| Styling   | Custom CSS (no Bootstrap needed)  |
| Fonts     | Google Fonts (Cinzel + Rajdhani)  |
| Build     | Maven                             |
| Lombok    | Yes (for boilerplate reduction)   |

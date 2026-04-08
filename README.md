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

## 🛠 Setup Instructions

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL 14+
- IntelliJ IDEA (recommended)

---

### 1. Create the PostgreSQL Database

```bash
# Connect as postgres superuser
psql -U postgres

# Create the database
CREATE DATABASE animevault;
\q
```

---

### 2. Configure `application.properties`

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/animevault
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

Change the admin credentials if desired:
```properties
app.admin.default-username=admin
app.admin.default-password=admin123
```

---

### 3. Run the Application

**In IntelliJ IDEA:**
1. Open the project (File → Open → select `animevault/` folder)
2. Wait for Maven to import dependencies
3. Run `AnimeVaultApplication.java`

**Or via Maven CLI:**
```bash
cd animevault
mvn spring-boot:run
```

The app starts at **http://localhost:8080**

---

### 4. First Login

On first startup, `DataInitializer` creates the default admin:

| Field    | Value      |
|----------|------------|
| Username | `admin`    |
| Password | `admin123` |

> ⚠️ **Change this password in `application.properties` before going to production!**

---

## 🔐 Access Control

| Route           | Who can access          |
|-----------------|-------------------------|
| `/`             | Everyone (anonymous OK) |
| `/browse`       | Everyone                |
| `/watch/**`     | Everyone                |
| `/search`       | Everyone                |
| `/auth/login`   | Anonymous only          |
| `/auth/register`| Anonymous only          |
| `/comments/post`| Logged-in users         |
| `/admin/**`     | ADMIN role only         |

---

## 📺 How to Upload Anime

1. Log in as admin → Go to **Admin Dashboard**
2. Click **Upload Anime**
3. Select **Series** or **Movie**
4. Fill in title, description, genres, thumbnail URL, rating
5. For **Movies**: add the video URL (MP4 or HLS stream)
6. For **Series**: after saving, you'll be redirected to the **Season Manager**
   - Add seasons (Season 1, Season 2, …)
   - Click **Episodes** on each season to add episodes with video URLs

---

## 🎥 Supported Video Formats

- Direct MP4/WebM file URLs (e.g. from a CDN or storage bucket)
- HLS streams (`.m3u8`) — use a CDN like Cloudflare Stream, Bunny.net, etc.
- Any URL the HTML5 `<video>` tag can play
- For DRM/encoding: integrate [Video.js](https://videojs.com/) or [HLS.js](https://hlsjs.video-dev.org/) by adding their scripts to `main.js`

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

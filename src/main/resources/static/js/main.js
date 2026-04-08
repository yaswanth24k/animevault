/* ═══════════════════════════════════════════════════════
   AnimeVault – Main JavaScript
═══════════════════════════════════════════════════════ */

// ── Mobile Menu ─────────────────────────────────────────
function toggleMobileMenu() {
    const menu = document.getElementById('mobileMenu');
    if (menu) menu.classList.toggle('open');
}

// Close mobile menu on outside click
document.addEventListener('click', function(e) {
    const menu = document.getElementById('mobileMenu');
    const btn = document.querySelector('.mobile-menu-btn');
    if (menu && btn && !menu.contains(e.target) && !btn.contains(e.target)) {
        menu.classList.remove('open');
    }
});

// ── Password Toggle ──────────────────────────────────────
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;
    input.type = input.type === 'password' ? 'text' : 'password';
}

// ── Auto-dismiss flash messages ──────────────────────────
document.addEventListener('DOMContentLoaded', function() {
    const flashes = document.querySelectorAll('.flash');
    flashes.forEach(flash => {
        setTimeout(() => {
            flash.style.transition = 'opacity 0.5s ease';
            flash.style.opacity = '0';
            setTimeout(() => flash.remove(), 500);
        }, 5000);
    });
});

// ── Active nav link highlight ────────────────────────────
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath === href) {
            link.style.color = 'var(--text)';
            link.style.background = 'rgba(255,255,255,0.06)';
        }
    });
});

// ── Lazy load images ─────────────────────────────────────
document.addEventListener('DOMContentLoaded', function() {
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    if (img.dataset.src) {
                        img.src = img.dataset.src;
                        img.removeAttribute('data-src');
                    }
                    imageObserver.unobserve(img);
                }
            });
        }, { rootMargin: '100px' });

        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }
});

// ── Video player enhancements ────────────────────────────
document.addEventListener('DOMContentLoaded', function() {
    const video = document.querySelector('.video-player');
    if (!video) return;

    // Remember playback position per episode (session storage key = src url)
    const storageKey = 'av_pos_' + encodeURIComponent(video.src || '');

    const savedTime = sessionStorage.getItem(storageKey);
    if (savedTime && parseFloat(savedTime) > 5) {
        video.addEventListener('loadedmetadata', function() {
            if (video.duration && parseFloat(savedTime) < video.duration - 10) {
                video.currentTime = parseFloat(savedTime);
            }
        }, { once: true });
    }

    // Save position every 5 seconds
    setInterval(function() {
        if (!video.paused && video.currentTime > 0) {
            sessionStorage.setItem(storageKey, video.currentTime.toString());
        }
    }, 5000);

    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        if (['INPUT', 'TEXTAREA', 'SELECT'].includes(document.activeElement.tagName)) return;
        if (e.key === ' ' || e.key === 'k') {
            e.preventDefault();
            video.paused ? video.play() : video.pause();
        }
        if (e.key === 'ArrowRight') { e.preventDefault(); video.currentTime += 10; }
        if (e.key === 'ArrowLeft')  { e.preventDefault(); video.currentTime -= 10; }
        if (e.key === 'f') {
            e.preventDefault();
            if (!document.fullscreenElement) {
                video.requestFullscreen?.();
            } else {
                document.exitFullscreen?.();
            }
        }
        if (e.key === 'm') { e.preventDefault(); video.muted = !video.muted; }
    });
});

// ── Confirm delete forms ─────────────────────────────────
document.addEventListener('DOMContentLoaded', function() {
    // Additional safety net for delete buttons (beyond inline onsubmit)
    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', function(e) {
            const msg = form.dataset.confirm || 'Are you sure?';
            if (!confirm(msg)) e.preventDefault();
        });
    });
});

// ── Smooth scroll for anchor links ──────────────────────
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            e.preventDefault();
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    });
});

// ── Card hover animation enhancement ────────────────────
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.anime-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.zIndex = '2';
        });
        card.addEventListener('mouseleave', function() {
            this.style.zIndex = '';
        });
    });
});

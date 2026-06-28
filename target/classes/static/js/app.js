const API_BASE = '/api';
const LOADER_API = 'http://localhost:8182/api/load';
let state = {
    token: localStorage.getItem('token'),
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    currentPage: 'films',
    films: [],
    sessions: [],
    users: [],
    bids: [],

};

const api = {
    async request(method, path, body) {
        const headers = { 'Content-Type': 'application/json' };
        if (state.token) {
            headers['Authorization'] = `Bearer ${state.token}`;
        }
        const res = await fetch(`${API_BASE}${path}`, {
            method,
            headers,
            body: body ? JSON.stringify(body) : undefined
        });
        if (res.status === 401 || res.status === 403) {
            logout();
            render();
            throw new Error('Unauthorized');
        }
        const text = await res.text();
        try {
            return text ? JSON.parse(text) : null;
        } catch {
            return text;
        }
    },
    get(path) { return this.request('GET', path); },
    post(path, body) { return this.request('POST', path, body); },
    delete(path) { return this.request('DELETE', path); }
};

function toast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const el = document.createElement('div');
    el.className = `toast toast-${type}`;
    el.textContent = message;
    container.appendChild(el);
    setTimeout(() => el.remove(), 3000);
}

function login(username, password) {
    api.post('/login', { username, password })
        .then(data => {
            if (data && data.token) {
                state.token = data.token;
                state.user = { username: data.username };
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(state.user));
                toast('Welcome!', 'success');
                render();
            } else {
                toast(data || 'Login failed', 'error');
            }
        })
        .catch(err => toast('Login failed: ' + err.message, 'error'));
}

function register(data) {
    api.post('/register', data)
        .then(msg => {
            toast(msg || 'Registration successful!', 'success');
            login(data.username, data.password);
        })
        .catch(err => toast('Registration failed: ' + err.message, 'error'));
}

function logout() {
    state.token = null;
    state.user = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
}

function isLoggedIn() { return !!state.token; }

function getRole() {
    if (!state.token) return null;
    try {
        const payload = JSON.parse(atob(state.token.split('.')[1]));
        const roles = payload.roles || [];
        if (roles.includes('ADMIN')) return 'ADMIN';
        if (roles.includes('MODERATOR')) return 'MODERATOR';
        return 'USER';
    } catch { return 'USER'; }
}

function hasRole(...roles) {
    return roles.includes(getRole());
}

function getPosterUrl(film) {
    return film?.posterUrl || film?.poster_url || null;
}

function getTrailerUrl(film) {
    return film?.trailerUrl || film?.trailer_url || null;
}

function renderPosterImg(url, alt) {
    if (!url) return '';
    return `<img src="${url}" alt="${escapeHtml(alt || '')}" loading="lazy" onerror="this.parentElement.innerHTML='<span style=\\'font-size:48px\\'>🎬</span>'">`;
}

function renderSkeletonGrid(count = 8) {
    return `<div class="films-grid page-fade-in">${Array(count).fill(0).map(() => `
        <div class="skeleton skeleton-card">
            <div class="skeleton-poster"></div>
            <div class="skeleton-body">
                <div class="skeleton-line" style="width:85%"></div>
                <div class="skeleton-line" style="width:55%"></div>
            </div>
        </div>
    `).join('')}</div>`;
}

function toggleSidebar() {
    document.getElementById('sidebar')?.classList.toggle('open');
}

function closeSidebar() {
    document.getElementById('sidebar')?.classList.remove('open');
}

function render() {
    if (!isLoggedIn()) {
        renderAuth();
        return;
    }
    renderApp();
}

// ===== Auth Page =====
function renderAuth() {
    const app = document.getElementById('app');
    app.innerHTML = `
        <div class="auth-page">
            <div class="auth-container">
                <div class="auth-header">
                    <div class="auth-logo">Film<span>2</span>Night</div>
                    <div class="auth-subtitle">Movie night — organize screenings with friends</div>
                </div>
                <div class="auth-box">
                    <div class="auth-tabs">
                        <button class="auth-tab active" data-tab="login">Login</button>
                        <button class="auth-tab" data-tab="register">Register</button>
                    </div>
                    <div id="auth-form-container">
                        ${renderLoginForm()}
                    </div>
                </div>
            </div>
        </div>
    `;

    document.querySelectorAll('.auth-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            const container = document.getElementById('auth-form-container');
            if (tab.dataset.tab === 'login') {
                container.innerHTML = renderLoginForm();
            } else {
                container.innerHTML = renderRegisterForm();
            }
            attachAuthListeners();
        });
    });
    attachAuthListeners();
}

function renderLoginForm() {
    return `
        <form class="auth-form" id="login-form">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Enter your username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit" class="btn btn-primary">Sign In</button>
            <div class="auth-error" id="auth-error"></div>
        </form>
    `;
}

function renderRegisterForm() {
    return `
        <form class="auth-form" id="register-form">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Choose a username" required>
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" name="firstName" placeholder="First Name" required>
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" name="lastName" placeholder="Last Name" required>
                </div>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Choose a password" required>
            </div>
            <button type="submit" class="btn btn-primary">Sign Up</button>
            <div class="auth-error" id="auth-error"></div>
        </form>
    `;
}

function attachAuthListeners() {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const fd = new FormData(loginForm);
            login(fd.get('username'), fd.get('password'));
        });
    }
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const fd = new FormData(registerForm);
            register({
                username: fd.get('username'),
                password: fd.get('password'),
                firstName: fd.get('firstName'),
                lastName: fd.get('lastName')
            });
        });
    }
}

// ===== App Layout =====
function renderApp() {
    const app = document.getElementById('app');
    const role = getRole();
    const navItems = getNavItems(role);

    app.innerHTML = `
        <div class="app-layout">
            <button class="hamburger" id="hamburger-btn" onclick="toggleSidebar()">☰</button>
            <aside class="sidebar" id="sidebar">
                <div class="sidebar-logo">Film<span>2</span>Night</div>
                <nav class="sidebar-nav">
                    ${navItems.map(item => `
                        <button class="nav-item ${state.currentPage === item.id ? 'active' : ''}" data-page="${item.id}">
                            <span class="nav-icon">${item.icon}</span>
                            ${item.label}
                        </button>
                    `).join('')}
                </nav>
                <div class="sidebar-footer">
                    <div class="user-info">
                        <div class="user-avatar">${(state.user?.username || '?')[0].toUpperCase()}</div>
                        <div class="user-details">
                            <div class="user-name">${state.user?.username || ''}</div>
                            <div class="user-role">${role === 'ADMIN' ? 'Administrator' : role === 'MODERATOR' ? 'Moderator' : 'User'}</div>
                        </div>
                        <button class="logout-btn" id="logout-btn">Sign out</button>
                    </div>
                </div>
            </aside>
            <div class="sidebar-overlay" onclick="closeSidebar()"></div>
            <main class="main-content" id="main-content">
                <div id="page-content"></div>
            </main>
        </div>
    `;

    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', () => {
            state.currentPage = item.dataset.page;
            closeSidebar();
            render();
        });
    });

    document.getElementById('logout-btn').addEventListener('click', () => {
        logout();
        render();
    });

    renderPage(state.currentPage);
}

function getNavItems(role) {
    const items = [
        { id: 'films', label: 'Movies', icon: '🎬' },
        { id: 'sessions', label: 'Sessions', icon: '🎫' }
    ];
    if (role === 'ADMIN' || role === 'MODERATOR') {
        items.push({ id: 'bids', label: 'Requests', icon: '📋' });
        items.push({ id: 'analytics', label: 'Analytics', icon: '📊' });
        items.push({ id: 'admin', label: 'Admin', icon: '⚙️' });
    }
    items.push({ id: 'profile', label: 'Profile', icon: '👤' });
    return items;
}

// ===== Page Router =====
function renderPage(page) {
    const container = document.getElementById('page-content');

    switch (page) {
        case 'films':
            container.innerHTML = `
                <div class="page-header">
                    <div>
                        <h1 class="page-title">Movies</h1>
                        <p class="page-subtitle">Choose a movie to watch</p>
                    </div>
                </div>
                ${renderSkeletonGrid(8)}
            `;
            loadFilmsPage();
            break;
        case 'sessions':
            container.innerHTML = `
                <div class="page-header">
                    <div>
                        <h1 class="page-title">Sessions</h1>
                        <p class="page-subtitle">Plan movie nights with friends</p>
                    </div>
                    <div class="page-actions">
                        <button class="btn btn-primary" onclick="showCreateSessionModal()">+ Create Session</button>
                    </div>
                </div>
                <div id="sessions-list"><div class="loading"><div class="spinner"></div>Loading...</div></div>
            `;
            loadSessionsPage();
            break;
        case 'analytics':
            container.innerHTML = `
                <div class="page-header">
                    <div>
                        <h1 class="page-title">Analytics</h1>
                        <p class="page-subtitle">Platform statistics</p>
                    </div>
                </div>
                <div id="analytics-content"><div class="loading"><div class="spinner"></div>Loading...</div></div>
            `;
            loadAnalyticsPage();
            break;
        case 'bids':
            container.innerHTML = `
                <div class="page-header">
                    <div>
                        <h1 class="page-title">Requests</h1>
                        <p class="page-subtitle">Manage user requests</p>
                    </div>
                    <div class="page-actions">
                        <button class="btn btn-sm btn-secondary" onclick="showBlockUserModal()">🚫 Block User</button>
                        \x3cbutton class="btn btn-sm btn-secondary" onclick="showBlockFilmModal()">🚫 Block Movie</button>
                    </div>
                </div>
                <div id="bids-content"><div class="loading"><div class="spinner"></div>Loading...</div></div>
            `;
            loadBidsPage();
            break;
        case 'admin':
            container.innerHTML = `
                <div class="page-header">
                    <div>
                        <h1 class="page-title">Administration</h1>
                        <p class="page-subtitle">Platform management</p>
                    </div>
                </div>
                <div id="admin-content"><div class="loading"><div class="spinner"></div>Loading...</div></div>
            `;
            loadAdminPage();
            break;
        case 'profile':
            loadProfilePage();
            break;
        case 'room':
            renderRoom();
            return;
        default:
            container.innerHTML = '<div class="empty-state"><div class="empty-icon">📄</div><div class="empty-title">Page not found</div></div>';
    }
}

// ===== Films Page =====
let filmsFilter = { view: 'all', sort: '', search: '', page: 0, size: 20, totalPages: 0, totalElements: 0 };

function renderPagination() {
    const { page, totalPages, totalElements, size } = filmsFilter;
    if (totalPages <= 1) return '';
    const start = Math.max(0, page - 2);
    const end = Math.min(totalPages - 1, page + 2);
    let html = `<div class="pagination">`;
    html += `<button class="btn btn-sm btn-secondary" onclick="goToPage(${page - 1})" ${page === 0 ? 'disabled' : ''}>← Prev</button>`;
    if (start > 0) {
        html += `<button class="btn btn-sm btn-secondary page-btn" onclick="goToPage(0)">1</button>`;
        if (start > 1) html += `<span class="page-ellipsis">...</span>`;
    }
    for (let i = start; i <= end; i++) {
        html += `<button class="btn btn-sm ${i === page ? 'btn-primary page-btn-active' : 'btn-secondary page-btn'}" onclick="goToPage(${i})">${i + 1}</button>`;
    }
    if (end < totalPages - 1) {
        if (end < totalPages - 2) html += `<span class="page-ellipsis">...</span>`;
        html += `<button class="btn btn-sm btn-secondary page-btn" onclick="goToPage(${totalPages - 1})">${totalPages}</button>`;
    }
    html += `<button class="btn btn-sm btn-secondary" onclick="goToPage(${page + 1})" ${page >= totalPages - 1 ? 'disabled' : ''}>Next →</button>`;
    const from = totalElements === 0 ? 0 : page * size + 1;
    const to = Math.min((page + 1) * size, totalElements);
    html += `<span class="pagination-info">${from}–${to} of ${totalElements}</span>`;
    html += `</div>`;
    return html;
}

function goToPage(n) {
    if (n < 0 || n >= filmsFilter.totalPages) return;
    filmsFilter.page = n;
    loadFilmsPage();
}

async function loadFilmsPage() {
    const container = document.getElementById('page-content');
    try {
        const sortParam = filmsFilter.sort ? '&sort=' + filmsFilter.sort : '';
        const searchParam = filmsFilter.search ? '&search=' + encodeURIComponent(filmsFilter.search) : '';
        const endpoint = filmsFilter.view === 'top250'
            ? '/films/top250?page=0&size=250'
            : `/films?page=${filmsFilter.page}&size=${filmsFilter.size}${sortParam}${searchParam}`;

        const data = await api.get(endpoint);
        const films = data?.content || [];
        state.films = films;
        if (data && data.totalPages !== undefined) {
            filmsFilter.totalPages = data.totalPages;
            filmsFilter.totalElements = data.totalElements;
        }

        container.innerHTML = `
            <div class="films-filter-bar page-fade-in">
                <div class="filter-tabs">
                    <button class="filter-tab ${filmsFilter.view === 'all' ? 'active' : ''}" data-view="all">All</button>
                    <button class="filter-tab ${filmsFilter.view === 'top250' ? 'active' : ''}" data-view="top250">Top 250</button>
                </div>
                <div class="filter-controls">
                    <select id="sort-select" class="filter-select">
                        <option value="">Default</option>
                        <option value="rating_kinopoisk,desc" ${filmsFilter.sort === 'rating_kinopoisk,desc' ? 'selected' : ''}>By Rating ↓</option>
                        <option value="filmYear,desc" ${filmsFilter.sort === 'filmYear,desc' ? 'selected' : ''}>By Year ↓</option>
                        <option value="filmYear,asc" ${filmsFilter.sort === 'filmYear,asc' ? 'selected' : ''}>By Year ↑</option>
                        <option value="name_origin,asc" ${filmsFilter.sort === 'name_origin,asc' ? 'selected' : ''}>By Name</option>
                    </select>
                    <div class="search-box">
                        <input type="text" id="search-input" placeholder="Search movies..." value="${filmsFilter.search}">
                        <button class="btn btn-sm btn-primary" onclick="applyFilmSearch()">🔍</button>
                    </div>
                </div>
            </div>
            ${films.length === 0 ? `
                <div class="empty-state page-fade-in">
                    <div class="empty-icon">🎬</div>
                    <div class="empty-title">No movies</div>
                    <p class="text-muted">No movies loaded yet</p>
                </div>
            ` : `
                <div class="films-grid page-fade-in">
                    ${films.map(film => `
                        <div class="film-card" data-film-id="${film.kinopoisk_id}">
                            <div class="film-poster">
                                ${(() => { const u = getPosterUrl(film); return u ? renderPosterImg(u, film.name_origin) : `<span>🎬</span>`; })()}
                                <div class="film-card-overlay">
                                    <div class="film-card-overlay-rating">⭐ ${film.rating_kinopoisk || 'N/A'}</div>
                                    <div class="film-card-overlay-year">${film.year || ''}</div>
                                </div>
                            </div>
                            <div class="film-info">
                                <div class="film-title">${film.name_origin}</div>
                                <div class="film-meta">
                                    <span class="film-rating">⭐ ${film.rating_kinopoisk || 'N/A'}</span>
                                    <span class="film-year">${film.year || ''}</span>
                                    <span class="film-length">${film.film_length ? film.film_length + ' min' : ''}</span>
                                </div>
                            </div>
                        </div>
                    `).join('')}
                </div>
                ${renderPagination()}
            `}
        `;

        document.querySelectorAll('.film-card').forEach(card => {
            card.addEventListener('click', () => showFilmModal(parseInt(card.dataset.filmId)));
        });

        document.querySelectorAll('.filter-tab').forEach(tab => {
            tab.addEventListener('click', () => {
                filmsFilter.view = tab.dataset.view;
                filmsFilter.sort = '';
                filmsFilter.search = '';
                filmsFilter.page = 0;
                loadFilmsPage();
            });
        });

        const sortSelect = document.getElementById('sort-select');
        if (sortSelect) {
            sortSelect.addEventListener('change', (e) => {
                filmsFilter.sort = e.target.value;
                filmsFilter.page = 0;
                loadFilmsPage();
            });
        }

        const searchInput = document.getElementById('search-input');
        if (searchInput) {
            searchInput.addEventListener('keydown', (e) => {
                if (e.key === 'Enter') applyFilmSearch();
            });
        }
    } catch (err) {
        container.innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Load error</div><p class="text-muted">${err.message}</p></div>`;
    }
}

function applyFilmSearch() {
    const val = document.getElementById('search-input').value.trim();
    filmsFilter.search = val;
    filmsFilter.view = 'all';
    filmsFilter.page = 0;
    loadFilmsPage();
}

async function showFilmModal(filmId) {
    try {
        const film = await api.get(`/film/${filmId}`);
        if (!film) { toast('Movie not found', 'error'); return; }

        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        const posterUrl = getPosterUrl(film);
        const hasPoster = !!posterUrl;
        overlay.innerHTML = `
            <div class="modal" style="max-width:640px">
                <div class="modal-title">${film.name_origin}</div>
                <div class="modal-film-layout">
                    <div class="modal-film-poster-side">
                        ${hasPoster ? renderPosterImg(posterUrl, film.name_origin) : `<div class="poster-fallback">🎬</div>`}
                    </div>
                    <div class="modal-film-details">
                        <div class="modal-film-rating-big">⭐ ${film.rating_kinopoisk || 'N/A'}</div>
                        <div class="modal-film-rating-label">${film.rating_kinopoisk_vote_count ? film.rating_kinopoisk_vote_count.toLocaleString() + ' ratings' : 'Kinopoisk Rating'}</div>
                        <div class="modal-film-meta-row">
                            <div class="modal-film-meta-item">
                                <span class="label">Year</span>
                                <span class="value">${film.year || 'N/A'}</span>
                            </div>
                            <div class="modal-film-meta-item">
                                <span class="label">Duration</span>
                                <span class="value">${film.film_length ? film.film_length + ' min' : 'N/A'}</span>
                            </div>
                            ${film.filmYear ? `
                            <div class="modal-film-meta-item">
                                <span class="label">Release Year</span>
                                <span class="value">${film.filmYear}</span>
                            </div>` : ''}
                        </div>
                        ${film.web_url ? `<a href="${film.web_url}" target="_blank" class="btn btn-sm btn-secondary" style="display:inline-flex;gap:6px;margin-top:16px">🌐 Watch on Kinopoisk</a>` : ''}
                        ${film.is_blocked ? '<div style="color:var(--accent);font-weight:600;margin-top:12px">⛔ Movie is blocked</div>' : ''}
                    </div>
                </div>
                <div class="modal-actions">
                    <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">Close</button>
                    <button class="btn btn-primary" onclick="createSessionFromFilm(${film.kinopoisk_id}); this.closest('.modal-overlay').remove()">Create Session</button>
                </div>
            </div>
        `;
        document.body.appendChild(overlay);
        overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

// ===== Sessions Page =====
async function loadSessionsPage() {
    const container = document.getElementById('page-content');
    try {
        const data = await api.get('/session/allSessions?page=0&size=50');
        const sessions = data?.content || [];
        state.sessions = sessions;

        const list = document.getElementById('sessions-list');

        if (sessions.length === 0) {
            list.innerHTML = `
                <div class="empty-state page-fade-in">
                    <div class="empty-icon">🎫</div>
                    <div class="empty-title">No active sessions</div>
                    <p class="text-muted">Create the first session!</p>
                </div>
            `;
            return;
        }

            const username = state.user?.username;
            list.innerHTML = sessions.map(session => {
                const isInSession = session.users?.some(u => u.username === username);
                const isFull = (session.visitorCount || 0) >= (session.maxVisitorCount || 0);
                const canJoin = !session.isCanceled && !isInSession && !isFull;
                const film = session.filmId;
                const posterUrl = getPosterUrl(film);
                return `
            <div class="session-detail-card page-fade-in">
                <div class="session-card-poster">
                    ${posterUrl ? renderPosterImg(posterUrl, film?.name_origin) : '🎬'}
                </div>
                <div class="session-card-body">
                <div class="session-detail-header">
                    <div>
                        <div class="session-film-title">${film?.name_origin || 'Unknown movie'}</div>
                        <div class="session-creator">Creator: ${session.creator?.username || 'N/A'}</div>
                    </div>
                    <div style="display:flex;gap:8px;flex-shrink:0">
                        ${session.isCanceled ? '<span class="badge badge-rejected">Canceled</span>' : '<span class="badge badge-approved">Active</span>'}
                    </div>
                </div>
                <div class="session-meta">
                    <div class="session-meta-item">
                        <span class="session-meta-label">Date & Time</span>
                        <span class="session-meta-value">${formatDate(session.startTime)}</span>
                    </div>
                    <div class="session-meta-item">
                        <span class="session-meta-label">Participants</span>
                        <span class="session-meta-value">${session.visitorCount || 0} / ${session.maxVisitorCount || 0}</span>
                    </div>
                    <div class="session-meta-item">
                        <span class="session-meta-label">Rating</span>
                        <span class="session-meta-value">⭐ ${session.averageRating ? session.averageRating.toFixed(1) : '—'} (${session.numberOfRatings || 0})</span>
                    </div>
                </div>
                ${session.users && session.users.length > 0 ? `
                    <div class="session-users">
                        ${session.users.map(u => `<span class="session-user-tag${u.username === username ? ' session-user-tag--me' : ''}">${u.username}${u.username === username ? ' (me)' : ''}</span>`).join('')}
                    </div>
                ` : ''}
                <div style="display:flex;gap:8px;margin-top:12px;flex-wrap:wrap">
                    <button class="btn btn-sm btn-primary" onclick="showSessionRoom(${session.id})">🎬 Enter Room</button>
                    ${canJoin ? `<button class="btn btn-sm btn-success" onclick="joinSession(${session.id})">+ Join</button>` : ''}
                    ${isInSession ? `<span class="badge badge-approved" style="padding:6px 12px;font-size:13px">✅ You're in</span>` : ''}
                    ${isFull && !isInSession ? `<span class="badge badge-rejected" style="padding:6px 12px;font-size:13px">❌ Full</span>` : ''}
                    <button class="btn btn-sm btn-secondary" onclick="rateSession(${session.id})">⭐ Rate</button>
                    ${!session.isCanceled && hasRole('ADMIN','MODERATOR') ? `<button class="btn btn-sm btn-danger" onclick="cancelSession(${session.id})">Cancel</button>` : ''}
                    ${!session.isCanceled ? `<button class="btn btn-sm btn-secondary" onclick="requestCancelSession(${session.id})">📋 Request Cancel</button>` : ''}
                </div>
                </div>
            </div>
        `}).join('');
    } catch (err) {
        document.getElementById('sessions-list').innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Load error</div><p class="text-muted">${err.message}</p></div>`;
    }
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { day: 'numeric', month: 'long', year: 'numeric', hour: '2-digit', minute: '2-digit' });
}

async function showCreateSessionModal(filmId) {
    if (state.films.length === 0) {
        try {
            const data = await api.get('/films?page=0&size=50');
            state.films = data?.content || [];
        } catch { }
    }

    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
        <div class="modal">
            <div class="modal-title">Create Session</div>
            <form id="create-session-form">
                <div class="form-group">
                    <label>Movie</label>
                    <select name="filmId" required>
                        <option value="">Select a movie</option>
                        ${state.films.map(f => `<option value="${f.kinopoisk_id}"${filmId && f.kinopoisk_id === filmId ? ' selected' : ''}>${f.name_origin}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group">
                    <label>Date & Time</label>
                    <input type="datetime-local" name="startTime" required>
                </div>
                <div class="form-group">
                    <label>Max Participants</label>
                    <input type="number" name="maxVisitorCount" value="10" min="2" max="50">
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Create</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(overlay);

    document.getElementById('create-session-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const fd = new FormData(e.target);
        const data = {
            filmId: parseInt(fd.get('filmId')),
            maxVisitorCount: parseInt(fd.get('maxVisitorCount')),
            startTime: fd.get('startTime') + ':00'
        };
        try {
            await api.post('/session/create', data);
            toast('Session created!', 'success');
            overlay.remove();
            loadSessionsPage();
        } catch (err) {
            toast('Error: ' + err.message, 'error');
        }
    });

    overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
}

function getYoutubeEmbedUrl(url, autoplay) {
    if (!url) return null;
    let videoId = null;
    const patterns = [
        /(?:youtube\.com\/embed\/)([a-zA-Z0-9_-]+)/,
        /(?:youtube\.com\/watch\?v=)([a-zA-Z0-9_-]+)/,
        /(?:youtu\.be\/)([a-zA-Z0-9_-]+)/,
        /(?:youtube\.com\/shorts\/)([a-zA-Z0-9_-]+)/
    ];
    for (const p of patterns) {
        const m = url.match(p);
        if (m) { videoId = m[1]; break; }
    }
    if (!videoId) return url;
    return `https://www.youtube.com/embed/${videoId}?rel=0&modestbranding=1${autoplay ? '&autoplay=1' : ''}`;
}

async function showSessionRoom(sessionId) {
    state.currentPage = 'room';
    state.roomSessionId = sessionId;
    renderRoom();
}

let roomInterval = null;

async function renderRoom() {
    const app = document.getElementById('app');
    const sessionId = state.roomSessionId;

    if (!sessionId) { state.currentPage = 'sessions'; render(); return; }

    if (roomInterval) { clearInterval(roomInterval); roomInterval = null; }

    app.innerHTML = `
        <div class="room-layout">
            <div class="room-left">
                <div class="loading"><div class="spinner"></div>Loading room...</div>
            </div>
            <div class="room-right">
                <div class="room-back">
                    <button class="btn btn-sm btn-secondary" onclick="leaveRoom()">← Back to Sessions</button>
                </div>
                <div class="room-chat">
                    <div class="room-chat-header">Chat</div>
                    <div class="room-messages" id="room-messages"><div class="loading"><div class="spinner"></div></div></div>
                    <form class="room-chat-input" id="room-chat-form">
                        <input type="text" name="msg" placeholder="Type a message..." required>
                        <button type="submit" class="btn btn-sm btn-primary">➤</button>
                    </form>
                </div>
                <div class="room-users">
                    <div class="room-users-header">Participants</div>
                    <div id="room-users-list"><div class="loading"><div class="spinner"></div></div></div>
                </div>
            </div>
        </div>
    `;

    let roomPoll = null;
    let trailerPlaying = false;
    let trailerStarted = false;
    let currentFilm = null;

    async function loadRoom() {
        try {
            const [sessionData, comments] = await Promise.all([
                api.get(`/session/${sessionId}`),
                api.get(`/comment/${sessionId}`)
            ]);

            const film = sessionData.filmId;
            currentFilm = film;
            const left = app.querySelector('.room-left');
            const username = state.user?.username;
            const trailerUrl = getTrailerUrl(film);
            const hasTrailer = trailerUrl && getYoutubeEmbedUrl(trailerUrl);
            const sessionTime = sessionData.startTime ? new Date(sessionData.startTime) : null;

            const trailerEmbedUrl = hasTrailer ? getYoutubeEmbedUrl(trailerUrl, false) : null;

            left.innerHTML = `
                <div class="room-back-mobile">
                    <button class="btn btn-sm btn-secondary" onclick="leaveRoom()">← Back</button>
                </div>
                <div class="room-film">
                    ${film?.poster_url ? `<div class="room-poster"><img src="${film.poster_url}" alt="${film.name_origin || ''}" onerror="this.parentElement.innerHTML='<div class=\\'room-poster-fallback\\'>🎬</div>'"></div>` : `<div class="room-poster"><div class="room-poster-fallback">🎬</div></div>`}
                    <div class="room-film-info">
                        <h1 class="room-film-title">${film?.name_origin || 'Unknown movie'}</h1>
                        <div class="room-film-meta">
                            <span>⭐ ${film?.rating_kinopoisk || 'N/A'}</span>
                            <span>📅 ${film?.filmYear || '—'}</span>
                            <span>⏱ ${film?.film_length ? film.film_length + ' min' : '—'}</span>
                        </div>
                        ${film?.web_url ? `<a href="${film.web_url}" target="_blank" class="btn btn-sm btn-secondary" style="display:inline-flex;align-items:center;gap:6px;margin-top:12px">🌐 Watch on Kinopoisk</a>` : ''}
                    </div>
                </div>
                ${hasTrailer ? `
                <div class="room-trailer" id="room-trailer-block">
                    <div class="room-trailer-header">
                        <h3 class="room-section-title">Trailer</h3>
                        <div class="room-trailer-badge" id="room-trailer-badge">
                            ${sessionTime && sessionTime > new Date() ? '<span class="badge badge-open">Before start</span>' : '<span class="badge badge-approved">Now</span>'}
                        </div>
                    </div>
                    <div class="room-trailer-player" id="room-trailer-player">
                        <div class="room-trailer-embed" id="room-trailer-embed">
                            <iframe id="trailer-iframe" src="${trailerEmbedUrl}" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
                        </div>
                        <div class="room-trailer-controls" id="room-trailer-controls">
                            <button class="btn btn-sm btn-primary" id="trailer-play-btn">⏸ Pause</button>
                            <button class="btn btn-sm btn-secondary" id="trailer-restart-btn">⟳ Restart</button>
                        </div>
                    </div>
                </div>` : `
                <div class="room-trailer" id="room-trailer-block">
                    <div class="room-trailer-header">
                        <h3 class="room-section-title">Trailer</h3>
                    </div>
                    <div class="room-trailer-empty">Trailer not available for this movie.</div>
                </div>`}
                <div class="room-session-info">
                    <h3 class="room-section-title">About Session</h3>
                    <div class="room-session-meta">
                        <span>🎫 Creator: <strong>${sessionData.creator?.username || 'N/A'}</strong></span>
                        <span>🕐 ${formatDate(sessionData.startTime)}</span>
                        <span>⭐ Rating: ${sessionData.averageRating ? sessionData.averageRating.toFixed(1) : '0.0'} (${sessionData.numberOfRatings || 0})</span>
                        ${sessionData.isCanceled ? '<span class="badge badge-rejected">Canceled</span>' : '<span class="badge badge-approved">Active</span>'}
                    </div>
                    <div id="room-countdown" class="room-countdown"></div>
                </div>
            `;

            renderRoomMessages(comments, username);
            renderRoomUsers(sessionData.users || [], username, sessionData.visitorCount, sessionData.maxVisitorCount);

            if (hasTrailer) {
                document.getElementById('trailer-play-btn').addEventListener('click', () => {
                    const iframe = document.getElementById('trailer-iframe');
                    if (trailerPlaying) {
                        iframe.src = getYoutubeEmbedUrl(trailerUrl, false);
                        document.getElementById('trailer-play-btn').textContent = '▶ Watch';
                    } else {
                        iframe.src = getYoutubeEmbedUrl(trailerUrl, true);
                        document.getElementById('trailer-play-btn').textContent = '⏸ Pause';
                    }
                    trailerPlaying = !trailerPlaying;
                    trailerStarted = true;
                });

                document.getElementById('trailer-restart-btn').addEventListener('click', () => {
                    const iframe = document.getElementById('trailer-iframe');
                    iframe.src = getYoutubeEmbedUrl(trailerUrl, true);
                    document.getElementById('trailer-play-btn').textContent = '⏸ Pause';
                    trailerPlaying = true;
                    trailerStarted = true;
                });
            }

            if (sessionTime) {
                updateCountdown(sessionTime);
            }
        } catch (err) {
            app.querySelector('.room-left').innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Load error</div><p class="text-muted">${err.message}</p></div>`;
        }
    }

    function updateCountdown(sessionTime) {
        const el = document.getElementById('room-countdown');
        if (!el) return;

        const now = new Date();
        const diff = sessionTime - now;

        if (diff <= 0) {
            el.innerHTML = `<div class="room-countdown-live">🔴 Live! Trailer started.</div>`;
            if (!trailerStarted && document.getElementById('trailer-iframe')) {
                const film = currentFilm;
                const url = getTrailerUrl(film);
                if (url && getYoutubeEmbedUrl(url)) {
                    const iframe = document.getElementById('trailer-iframe');
                    iframe.src = getYoutubeEmbedUrl(url, true);
                    trailerPlaying = true;
                    trailerStarted = true;
                    const btn = document.getElementById('trailer-play-btn');
                    if (btn) btn.textContent = '⏸ Pause';
                    toast('🎬 Session started! Trailer playing.', 'success');
                }
            }
            return;
        }

        const minutes = Math.floor(diff / 60000);
        const seconds = Math.floor((diff % 60000) / 1000);
        el.innerHTML = `
            <div class="room-countdown-wait">
                <span class="room-countdown-icon">⏳</span>
                <span class="room-countdown-text">Trailer starts in <strong>${minutes}:${seconds.toString().padStart(2, '0')}</strong></span>
            </div>
        `;
    }

    function renderRoomMessages(comments, username) {
        const container = document.getElementById('room-messages');
        if (!container) return;
        if (!comments || comments.length === 0) {
            container.innerHTML = '<div class="room-empty-msg">No messages yet. Be the first!</div>';
            return;
        }
        container.innerHTML = comments.map(c => `
            <div class="room-msg ${c.author === username ? 'room-msg--me' : ''}">
                <div class="room-msg-author">${c.author}</div>
                <div class="room-msg-text">${escapeHtml(c.text)}</div>
                <div class="room-msg-time">${formatTime(c.createdAt)}</div>
            </div>
        `).join('');
        container.scrollTop = container.scrollHeight;
    }

    function renderRoomUsers(users, username, visitorCount, maxVisitorCount) {
        const container = document.getElementById('room-users-list');
        if (!container) return;
        container.innerHTML = `
            <div class="room-users-count">${visitorCount || 0} / ${maxVisitorCount || 0}</div>
            ${users.map(u => `
                <div class="room-user ${u.username === username ? 'room-user--me' : ''}">
                    <div class="room-user-avatar">${(u.username || '?')[0].toUpperCase()}</div>
                    <span>${u.username}${u.username === username ? ' (me)' : ''}</span>
                </div>
            `).join('')}
        `;
    }

    async function pollComments() {
        if (state.currentPage !== 'room') { if (roomPoll) clearInterval(roomPoll); return; }
        try {
            const comments = await api.get(`/comment/${sessionId}`);
            const username = state.user?.username;
            renderRoomMessages(comments, username);
        } catch {}
    }

    loadRoom().then(() => {
        roomPoll = setInterval(pollComments, 3000);
        roomInterval = setInterval(() => {
            if (state.currentPage !== 'room') { clearInterval(roomInterval); return; }
            api.get(`/session/${sessionId}`).then(sd => {
                if (sd.startTime) updateCountdown(new Date(sd.startTime));
            }).catch(() => {});
        }, 5000);
    });

    document.getElementById('room-chat-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const input = e.target.querySelector('input');
        const text = input.value.trim();
        if (!text) return;
        input.value = '';
        try {
            await api.post('/comment/add', { sessionId, commentText: text });
            const comments = await api.get(`/comment/${sessionId}`);
            const username = state.user?.username;
            renderRoomMessages(comments, username);
        } catch (err) {
            toast('Error: ' + err.message, 'error');
        }
    });
}

function leaveRoom() {
    state.currentPage = 'sessions';
    if (roomInterval) { clearInterval(roomInterval); roomInterval = null; }
    render();
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatTime(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
}

async function joinSession(sessionId) {
    try {
        await api.post(`/session/join/${sessionId}`);
        toast('You joined the session!', 'success');
        loadSessionsPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function rateSession(sessionId) {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
        <div class="modal" style="max-width:400px">
            <div class="modal-title">Rate Session</div>
            <p style="color:var(--text-secondary);margin-bottom:16px">Rating from 0 to 10</p>
            <div class="stars" id="rating-stars">
                ${[1,2,3,4,5,6,7,8,9,10].map(i => `<span class="star" data-value="${i}">★</span>`).join('')}
            </div>
            <p style="text-align:center;margin-top:12px;font-size:18px;font-weight:600">Rating: <span id="rating-value">0</span></p>
            <div class="modal-actions">
                <button class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">Cancel</button>
                <button class="btn btn-primary" id="submit-rating">Rate</button>
            </div>
        </div>
    `;
    document.body.appendChild(overlay);

    let selectedRating = 0;
    document.querySelectorAll('.star').forEach(star => {
        star.addEventListener('click', () => {
            selectedRating = parseInt(star.dataset.value);
            document.querySelectorAll('.star').forEach(s => {
                s.classList.toggle('active', parseInt(s.dataset.value) <= selectedRating);
            });
            document.getElementById('rating-value').textContent = selectedRating;
        });
    });

    document.getElementById('submit-rating').addEventListener('click', async () => {
        if (selectedRating === 0) { toast('Select a rating', 'info'); return; }
        try {
            await api.post('/rating/add', { sessionId, rating: selectedRating });
            toast('Rating submitted!', 'success');
            overlay.remove();
            loadSessionsPage();
        } catch (err) {
            toast('Error: ' + err.message, 'error');
        }
    });

    overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
}

async function cancelSession(sessionId) {
    if (!confirm('Cancel this session?')) return;
    try {
        await api.delete(`/session/cancel/${sessionId}`);
        toast('Session canceled', 'success');
        loadSessionsPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function createSessionFromFilm(filmId) {
    state.currentPage = 'sessions';
    render();
    setTimeout(() => showCreateSessionModal(filmId), 500);
}

// ===== Bids Page =====
const BID_TYPES = {
    ADD_USER_TO_SESSION: 'Add to Session',
    DELETE_SESSION: 'Cancel Session',
    DELETE_FILM: 'Block Movie',
    DELETE_USER: 'Block User'
};

async function loadBidsPage() {
    const container = document.getElementById('page-content');
    container.innerHTML = `
        <div class="page-header">
            <div>
                <h1 class="page-title">Requests</h1>
                <p class="page-subtitle">Manage user requests</p>
            </div>
        </div>
        <div id="bids-content"><div class="loading"><div class="spinner"></div>Loading...</div></div>
    `;

    try {
        const bids = await api.get('/bid/all');
        const list = document.getElementById('bids-content');

        if (!bids || bids.length === 0) {
            list.innerHTML = `<div class="empty-state page-fade-in"><div class="empty-icon">📋</div><div class="empty-title">No requests</div><p class="text-muted">Requests are created when blocking a user/movie or canceling a session</p></div>`;
            return;
        }

        list.innerHTML = `
            <div class="table-container page-fade-in">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>User</th>
                            <th>Movie / Session</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${bids.map(bid => {
                            const statusLower = (bid.statusType || bid.status || '').toLowerCase();
                            const badgeClass = statusLower === 'approved' ? 'approved' : statusLower === 'rejected' ? 'rejected' : 'open';
                            const filmName = bid.filmId?.name_origin || '—';
                            return `
                            <tr>
                                <td>#${bid.bidId || bid.id}</td>
                                <td>${BID_TYPES[bid.bidType] || bid.bidType || '—'}</td>
                                <td>${bid.username || '—'}</td>
                                <td style="font-size:12px">${filmName} ${bid.sessionId ? '(session #' + bid.sessionId + ')' : ''}</td>
                                <td><span class="badge badge-${badgeClass}">${(bid.statusType || bid.status || '—').toUpperCase()}</span></td>
                                <td>
                                    ${(bid.statusType || bid.status) === 'OPEN' ? `
                                        <div style="display:flex;gap:4px">
                                            <button class="btn btn-sm btn-success" onclick="approveBid(${bid.bidId || bid.id}, '${bid.bidType}')">Approve</button>
                                            <button class="btn btn-sm btn-danger" onclick="rejectBid(${bid.bidId || bid.id})">Reject</button>
                                        </div>
                                    ` : '—'}
                                </td>
                            </tr>
                        `}).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } catch (err) {
        document.getElementById('bids-content').innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Error</div></div>`;
    }
}

async function approveBid(id, bidType) {
    let endpoint;
    switch (bidType) {
        case 'ADD_USER_TO_SESSION': endpoint = `/bid/addUser/create/approve/${id}`; break;
        case 'DELETE_SESSION': endpoint = `/bid/session/delete/approve/${id}`; break;
        case 'DELETE_FILM': endpoint = `/bid/film/delete/approve/${id}`; break;
        case 'DELETE_USER': endpoint = `/bid/user/block/approve/${id}`; break;
        default: toast('Unknown request type', 'error'); return;
    }
    try {
        await api.post(endpoint);
        toast('Request approved', 'success');
        loadBidsPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function rejectBid(id) {
    try {
        await api.post(`/bid/reject/${id}`);
        toast('Request rejected', 'success');
        loadBidsPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

function showBlockFilmModal() {
    if (state.films.length === 0) { toast('Load movies first', 'info'); return; }
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
        <div class="modal" style="max-width:400px">
            <div class="modal-title">🚫 Block Movie</div>
            <form id="block-film-form">
                <div class="form-group">
                    <label>Movie</label>
                    <select name="filmId" required>
                        <option value="">Select a movie</option>
                        ${state.films.map(f => `<option value="${f.kinopoisk_id}">${f.name_origin}</option>`).join('')}
                    </select>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">Cancel</button>
                    <button type="submit" class="btn btn-danger">Block</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(overlay);
    document.getElementById('block-film-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const filmId = parseInt(e.target.querySelector('select').value);
        try {
            await api.post('/bid/film/delete', { filmId, blockId: 1 });
            toast('Block request created', 'success');
            overlay.remove();
            loadBidsPage();
        } catch (err) {
            toast('Error: ' + err.message, 'error');
        }
    });
    overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
}

function showBlockUserModal() {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = `
        <div class="modal" style="max-width:400px">
            <div class="modal-title">🚫 Block User</div>
            <form id="block-user-form">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" name="username" placeholder="Enter username" required>
                </div>
                <div class="modal-actions">
                    <button type="button" class="btn btn-secondary" onclick="this.closest('.modal-overlay').remove()">Cancel</button>
                    <button type="submit" class="btn btn-danger">Block</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(overlay);

    document.getElementById('block-user-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = e.target.querySelector('input').value.trim();
        try {
            const result = await api.post('/bid/user/block', { username });
            toast('Block request created', 'success');
            overlay.remove();
            loadBidsPage();
        } catch (err) {
            toast('Error: ' + err.message, 'error');
        }
    });
    overlay.addEventListener('click', (e) => { if (e.target === overlay) overlay.remove(); });
}

// ===== Admin Page =====
async function loadAdminPage() {
    const container = document.getElementById('page-content');

    try {
        let stats = {}, users = [];
        try {
            const currentYear = new Date().getFullYear();
            const [yearCount, sessions, usersData] = await Promise.all([
                api.get(`/session/count/active/${currentYear}`),
                api.get('/session/allSessions?page=0&size=1'),
                api.get('/users')
            ]);
            stats = { yearCount, totalSessions: sessions?.totalElements || 0 };
            users = usersData || [];
        } catch { }

        const content = document.getElementById('admin-content');
        content.innerHTML = `
            <div class="stats-grid page-fade-in">
                <div class="stat-card">
                    <div class="stat-value" style="color:var(--accent)">${stats.totalSessions || '—'}</div>
                    <div class="stat-label">Total Sessions</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value" style="color:var(--gold)">${stats.yearCount || '—'}</div>
                    <div class="stat-label">Sessions in ${new Date().getFullYear()}</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value" style="color:var(--green)">${state.films.length || '—'}</div>
                    <div class="stat-label">Movies</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value" style="color:var(--orange)">${users.length || '—'}</div>
                    <div class="stat-label">Users</div>
                </div>
            </div>

            <div class="session-detail-card mt-4 page-fade-in" style="display:block">
                <h3 style="margin-bottom:12px;font-size:14px;color:var(--text-secondary);text-transform:uppercase;letter-spacing:0.5px">Movie Management</h3>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Title</th><th>Rating</th><th>Status</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                            ${state.films.map(f => `
                                <tr>
                                    <td>${f.kinopoisk_id}</td>
                                    <td>${f.name_origin}</td>
                                    <td><span style="color:var(--gold)">⭐ ${f.rating_kinopoisk || 'N/A'}</span></td>
                                    <td>${f.is_blocked ? '<span class="badge badge-rejected">Blocked</span>' : '<span class="badge badge-approved">Active</span>'}</td>
                                    <td>${!f.is_blocked ? `<button class="btn btn-sm btn-danger" onclick="requestBlockFilm(${f.kinopoisk_id})">🚫 Block</button>` : ''}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="session-detail-card mt-4 page-fade-in" style="display:block">
                <h3 style="margin-bottom:12px;font-size:14px;color:var(--text-secondary);text-transform:uppercase;letter-spacing:0.5px">Users</h3>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr><th>ID</th><th>Name</th><th>Login</th><th>Status</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                            ${users.map(u => `
                                <tr>
                                    <td>${u.id}</td>
                                    <td>${u.firstName || ''} ${u.lastName || ''}</td>
                                    <td>${u.username}</td>
                                    <td>${u.isBlocked || u.is_blocked ? '<span class="badge badge-rejected">Blocked</span>' : '<span class="badge badge-approved">Active</span>'}</td>
                                    <td>${!(u.isBlocked || u.is_blocked) ? `<button class="btn btn-sm btn-danger" onclick="requestBlockUser('${u.username}')">🚫 Block</button>` : ''}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="session-detail-card mt-4 page-fade-in" style="display:block">
                <h3 style="margin-bottom:12px;font-size:14px;color:var(--text-secondary);text-transform:uppercase;letter-spacing:0.5px">Movie Loader (Kinopoisk)</h3>
                <div style="display:flex;gap:12px;flex-wrap:wrap;align-items:center">
                    <input type="number" id="load-film-id" placeholder="Movie ID" style="width:120px;padding:8px 12px;background:var(--bg-input);border:1px solid var(--border);border-radius:var(--radius-xs);color:var(--text-primary);outline:none">
                    <button class="btn btn-sm btn-primary" onclick="loadFilmFromKinopoisk()">📥 Load</button>
                    <button class="btn btn-sm btn-secondary" onclick="loadTop250()">🏆 Top250</button>
                    <button class="btn btn-sm btn-secondary" onclick="loadAdminPage()">🔄 Refresh</button>
                </div>
                <div id="load-result" style="margin-top:8px;font-size:14px"></div>
                <div style="margin-top:12px;display:flex;gap:12px;flex-wrap:wrap;align-items:center">
                    <button class="btn btn-sm btn-success" onclick="syncTop250ToFilms()">📦 Sync Top250</button>
                </div>
            </div>
        `;
    } catch (err) {
        document.getElementById('admin-content').innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Error</div></div>`;
    }
}

async function requestBlockFilm(filmId) {
    if (!confirm('Create a block request for this movie?')) return;
    try {
        await api.post('/bid/film/delete', { filmId, blockId: 1 });
        toast('Block request created', 'success');
        loadAdminPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function requestBlockUser(username) {
    if (!confirm(`Create a block request for user ${username}?`)) return;
    try {
        await api.post('/bid/user/block', { username });
        toast('Block request created', 'success');
        loadAdminPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function requestCancelSession(sessionId) {
    if (!confirm('Create a cancel request for this session?')) return;
    try {
        await api.post(`/bid/session/delete/${sessionId}`);
        toast('Cancel request created', 'success');
        loadSessionsPage();
    } catch (err) {
        toast('Error: ' + err.message, 'error');
    }
}

async function loadFilmFromKinopoisk() {
    const id = document.getElementById('load-film-id').value;
    if (!id) return toast('Enter movie ID', 'error');
    const btn = document.querySelector('button[onclick="loadFilmFromKinopoisk()"]');
    btn.disabled = true; btn.textContent = '⏳ Loading...';
    try {
        const res = await fetch(`${LOADER_API}/film/${id}`, { method: 'POST' });
        const text = await res.text();
        toast(text, res.ok ? 'success' : 'error');
        if (res.ok) setTimeout(loadAdminPage, 1500);
    } catch (e) {
        toast('Error: ' + e.message, 'error');
    } finally {
        btn.disabled = false; btn.textContent = '📥 Load';
    }
}
async function loadTop250() {
    const btn = document.querySelector('button[onclick="loadTop250()"]');
    btn.disabled = true; btn.textContent = '⏳ Loading...';
    try {
        const res = await fetch(`${LOADER_API}/top250`, { method: 'POST' });
        const text = await res.text();
        toast(text, res.ok ? 'success' : 'error');
    } catch (e) {
        toast('Error: ' + e.message, 'error');
    } finally {
        btn.disabled = false; btn.textContent = '🏆 Top250';
    }
}
async function syncTop250ToFilms() {
    const btn = document.querySelector('button[onclick="syncTop250ToFilms()"]');
    btn.disabled = true; btn.textContent = '⏳ Syncing... (may take 5-10 min)';
    try {
        const res = await fetch(`${LOADER_API}/top250/full`, { method: 'POST' });
        const text = await res.text();
        toast(text, res.ok ? 'success' : 'error');
        if (res.ok) setTimeout(loadAdminPage, 2000);
    } catch (e) {
        toast('Error: ' + e.message, 'error');
    } finally {
        btn.disabled = false; btn.textContent = '📦 Sync Top250';
    }
}

// ===== Analytics Page =====
async function loadAnalyticsPage() {
    const container = document.getElementById('analytics-content');
    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;

    try {
        const [sessionsYear, sessionsMonth, sessionsWeek, sessionsDay,
               lastYear, ratings] = await Promise.all([
            api.get(`/session/count/active/${currentYear}`).catch(() => null),
            api.get(`/session/count/active/month/${currentYear}/${currentMonth}`).catch(() => null),
            api.get(`/sessions/count/active/week/${currentYear}/${getCurrentWeek()}`).catch(() => null),
            api.get(`/sessions/count/active/day/${currentYear}/${currentMonth}/${new Date().getDate()}`).catch(() => null),
            api.get('/session/lastYear').catch(() => []),
            api.get(`/sessions/rating/${currentYear}/${currentMonth}`).catch(() => [])
        ]);

        container.innerHTML = `
            <div class="page-fade-in">
                <div class="analytics-section">
                    <h3 class="analytics-section-title">📅 Sessions</h3>
                    <div class="stats-grid">
                        <div class="stat-card"><div class="stat-value" style="color:var(--accent)">${sessionsYear ?? '—'}</div><div class="stat-label">This Year</div></div>
                        <div class="stat-card"><div class="stat-value" style="color:var(--gold)">${sessionsMonth ?? '—'}</div><div class="stat-label">This Month</div></div>
                        <div class="stat-card"><div class="stat-value" style="color:var(--green)">${sessionsWeek ?? '—'}</div><div class="stat-label">This Week</div></div>
                        <div class="stat-card"><div class="stat-value" style="color:var(--orange)">${sessionsDay ?? '—'}</div><div class="stat-label">Today</div></div>
                    </div>
                </div>

                <div class="analytics-section">
                    <h3 class="analytics-section-title">🏆 Top Rated Sessions (${getMonthName(currentMonth)} ${currentYear})</h3>
                    ${renderRatingTable(ratings)}
                </div>

                <div class="analytics-section">
                    <h3 class="analytics-section-title">📋 All Sessions (Last 12 Months)</h3>
                    ${renderLastYearSessions(lastYear)}
                </div>

                <div class="analytics-section">
                    <h3 class="analytics-section-title">📆 View by Period</h3>
                    <form id="analytics-period-form" class="analytics-period-form">
                        <div class="form-row" style="display:flex;gap:12px;align-items:end;flex-wrap:wrap">
                            <div class="form-group" style="flex:1;min-width:120px">
                                <label>Year</label>
                                <select name="year">${generateYearOptions(currentYear)}</select>
                            </div>
                            <div class="form-group" style="flex:1;min-width:120px">
                                <label>Month</label>
                                <select name="month">${generateMonthOptions(currentMonth)}</select>
                            </div>
                            <button type="submit" class="btn btn-sm btn-primary">View</button>
                        </div>
                    </form>
                    <div id="analytics-period-result" style="margin-top:16px"></div>
                </div>
            </div>
        `;

        document.getElementById('analytics-period-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const fd = new FormData(e.target);
            const year = parseInt(fd.get('year'));
            const month = parseInt(fd.get('month'));
            const resultDiv = document.getElementById('analytics-period-result');
            resultDiv.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
            try {
                const [sessionCount, userCount, ratingList] = await Promise.all([
                    api.get(`/session/count/active/month/${year}/${month}`).catch(() => null),
                    api.get(`/users/active/month/${year}/${month}`).catch(() => null),
                    api.get(`/sessions/rating/${year}/${month}`).catch(() => [])
                ]);
                resultDiv.innerHTML = `
                    <div class="stats-grid" style="margin-bottom:16px">
                        <div class="stat-card"><div class="stat-value" style="color:var(--accent)">${sessionCount ?? '—'}</div><div class="stat-label">Sessions in ${getMonthName(month)} ${year}</div></div>
                        <div class="stat-card"><div class="stat-value" style="color:var(--gold)">${userCount ?? '—'}</div><div class="stat-label">Active Users in ${getMonthName(month)} ${year}</div></div>
                    </div>
                    <h4 style="margin-bottom:8px;font-size:13px;color:var(--text-secondary)">Session Ratings</h4>
                    ${renderRatingTable(ratingList)}
                `;
            } catch (err) {
                resultDiv.innerHTML = '<p class="text-muted">Error loading period data</p>';
            }
        });
    } catch (err) {
        container.innerHTML = `<div class="empty-state"><div class="empty-icon">⚠️</div><div class="empty-title">Error</div></div>`;
    }
}

function renderRatingTable(ratings) {
    if (!ratings || ratings.length === 0) return '<p class="text-muted">No ratings this period</p>';
    return `
        <div class="table-container">
            <table>
                <thead>
                    <tr><th>Session ID</th><th>Avg Rating</th></tr>
                </thead>
                <tbody>
                    ${ratings.map(r => `
                        <tr><td>#${r.sessionId || r.id}</td><td>⭐ ${typeof r.averageRating === 'number' ? r.averageRating.toFixed(1) : r.averageRating || '—'}</td></tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function renderLastYearSessions(sessions) {
    if (!sessions || sessions.length === 0) return '<p class="text-muted">No sessions in the last 12 months</p>';
    return `
        <div class="table-container">
            <table>
                <thead>
                    <tr><th>ID</th><th>Movie</th><th>Creator</th><th>Date</th><th>Status</th></tr>
                </thead>
                <tbody>
                    ${sessions.slice(0, 20).map(s => `
                        <tr>
                            <td>#${s.id}</td>
                            <td>${s.filmId?.name_origin || '—'}</td>
                            <td>${s.creator?.username || '—'}</td>
                            <td>${formatDate(s.startTime)}</td>
                            <td>${s.isCanceled ? '<span class="badge badge-rejected">Canceled</span>' : '<span class="badge badge-approved">Active</span>'}</td>
                        </tr>
                    `).join('')}
                    ${sessions.length > 20 ? `<tr><td colspan="5" style="text-align:center;color:var(--text-secondary)">… and ${sessions.length - 20} more</td></tr>` : ''}
                </tbody>
            </table>
        </div>
    `;
}

function getCurrentWeek() {
    const now = new Date();
    const startOfYear = new Date(now.getFullYear(), 0, 1);
    const diff = now - startOfYear;
    return Math.ceil((diff / 86400000 + startOfYear.getDay() + 1) / 7);
}

function getMonthName(m) { return ['January','February','March','April','May','June','July','August','September','October','November','December'][m - 1] || ''; }

function generateYearOptions(currentYear) {
    return Array.from({length: 5}, (_, i) => currentYear - i).map(y => `<option value="${y}">${y}</option>`).join('');
}

function generateMonthOptions(currentMonth) {
    return Array.from({length: 12}, (_, i) => i + 1).map(m => `<option value="${m}" ${m === currentMonth ? 'selected' : ''}>${getMonthName(m)}</option>`).join('');
}

// ===== Profile Page =====
async function loadProfilePage() {
    const container = document.getElementById('page-content');
    const role = getRole();
    const roleLabel = role === 'ADMIN' ? 'Administrator' : role === 'MODERATOR' ? 'Moderator' : 'User';
    container.innerHTML = `
        <div class="page-header">
            <div>
                <h1 class="page-title">Profile</h1>
                <p class="page-subtitle">Your data and sessions</p>
            </div>
        </div>
        <div class="session-detail-card profile-card page-fade-in">
            <div class="profile-avatar">${(state.user?.username || '?')[0].toUpperCase()}</div>
            <div>
                <h2 style="font-size:24px;font-weight:700">${state.user?.username || 'N/A'}</h2>
                <p style="color:var(--text-secondary);margin-top:4px">Role: <span class="badge badge-approved">${roleLabel}</span></p>
            </div>
        </div>
    `;
}

// ===== Init =====
render();

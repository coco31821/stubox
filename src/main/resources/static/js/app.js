const ACCESS_TOKEN_KEY = "stubox.accessToken";
const REFRESH_TOKEN_KEY = "stubox.refreshToken";

function getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
}

function setTokens(tokenResponse) {
    localStorage.setItem(ACCESS_TOKEN_KEY, tokenResponse.accessToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, tokenResponse.refreshToken);
}

function clearTokens() {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
}

function isAuthenticated() {
    return Boolean(getAccessToken());
}

function updateAuthUi() {
    document.querySelectorAll(".auth-only").forEach((element) => {
        element.classList.toggle("hidden", !isAuthenticated());
    });
    document.querySelectorAll(".guest-only").forEach((element) => {
        element.classList.toggle("hidden", isAuthenticated());
    });
}

async function requestJson(url, options = {}) {
    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {}),
    };

    const token = getAccessToken();
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        ...options,
        headers,
    });
    const body = await response.json().catch(() => null);

    if (!response.ok || body?.success === false) {
        throw new Error(body?.message || "요청을 처리하지 못했습니다.");
    }

    return body;
}

function getFormValues(form) {
    return Object.fromEntries(new FormData(form).entries());
}

function alertAndFocus(message, form) {
    alert(message);
    form?.querySelector("input, textarea, select")?.focus();
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function setupLogin() {
    const form = document.querySelector("[data-login-form]");
    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            const response = await requestJson("/api/v1/auth/login", {
                method: "POST",
                body: JSON.stringify(getFormValues(form)),
            });
            setTokens(response.data);
            location.href = "/posts";
        } catch (error) {
            alertAndFocus(error.message, form);
        }
    });
}

function setupRegister() {
    const form = document.querySelector("[data-register-form]");
    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            await requestJson("/api/v1/users/register", {
                method: "POST",
                body: JSON.stringify(getFormValues(form)),
            });
            alert("가입되었습니다. 로그인해주세요.");
            location.href = "/login";
        } catch (error) {
            alertAndFocus(error.message, form);
        }
    });
}

function setupLogout() {
    document.querySelectorAll("[data-logout]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await requestJson("/api/v1/auth/logout", {
                    method: "POST",
                    body: JSON.stringify({}),
                });
            } catch (error) {
                // Token cleanup should still happen when the server already rejected it.
            } finally {
                clearTokens();
                location.href = "/posts";
            }
        });
    });
}

function setupPostForm() {
    const form = document.querySelector("[data-post-form]");
    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const postId = form.dataset.postId;
        const values = getFormValues(form);
        const payload = postId
            ? { title: values.title, content: values.content }
            : { boardId: Number(values.boardId), title: values.title, content: values.content };

        try {
            const response = await requestJson(postId ? `/api/v1/posts/${postId}` : "/api/v1/posts", {
                method: postId ? "PATCH" : "POST",
                body: JSON.stringify(payload),
            });
            location.href = `/posts/${response.data.postId}`;
        } catch (error) {
            alertAndFocus(error.message, form);
        }
    });
}

function setupBoardForm() {
    const dialog = document.querySelector("[data-board-dialog]");
    const form = document.querySelector("[data-board-form]");
    const openButton = document.querySelector("[data-open-board-dialog]");
    const closeButton = document.querySelector("[data-close-board-dialog]");
    if (!dialog || !form || !openButton) {
        return;
    }

    openButton.addEventListener("click", () => {
        form.reset();
        dialog.showModal();
        form.querySelector("input")?.focus();
    });

    closeButton?.addEventListener("click", () => {
        dialog.close();
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const values = getFormValues(form);
        try {
            const response = await requestJson("/api/v1/boards", {
                method: "POST",
                body: JSON.stringify({ name: values.name.trim() }),
            });
            location.href = `/posts?boardId=${response.data.boardId}`;
        } catch (error) {
            alertAndFocus(error.message, form);
        }
    });
}

function setupPostDelete() {
    document.querySelectorAll("[data-delete-post]").forEach((button) => {
        button.addEventListener("click", async () => {
            if (!confirm("게시글을 삭제할까요?")) {
                return;
            }

            try {
                await requestJson(`/api/v1/posts/${button.dataset.deletePost}`, {
                    method: "DELETE",
                });
                location.href = "/posts";
            } catch (error) {
                alert(error.message);
            }
        });
    });
}

function setupCommentForm() {
    const form = document.querySelector("[data-comment-form]");
    if (!form) {
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        try {
            await requestJson(`/api/v1/posts/${form.dataset.postId}/comments`, {
                method: "POST",
                body: JSON.stringify(getFormValues(form)),
            });
            location.reload();
        } catch (error) {
            alertAndFocus(error.message, form);
        }
    });
}

function setupCommentDelete() {
    document.querySelectorAll("[data-delete-comment]").forEach((button) => {
        button.addEventListener("click", async () => {
            if (!confirm("댓글을 삭제할까요?")) {
                return;
            }

            try {
                await requestJson(`/api/v1/comments/${button.dataset.deleteComment}`, {
                    method: "DELETE",
                });
                location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });
}

async function setupMe() {
    const panel = document.querySelector("[data-me-panel]");
    if (!panel || !isAuthenticated()) {
        return;
    }

    try {
        const response = await requestJson("/api/v1/users/me");
        const user = response.data;
        panel.innerHTML = `
            <dl class="grid gap-4 sm:grid-cols-2">
                <div>
                    <dt class="text-sm text-base-content/60">이메일</dt>
                    <dd class="mt-1 font-semibold">${escapeHtml(user.email)}</dd>
                </div>
                <div>
                    <dt class="text-sm text-base-content/60">닉네임</dt>
                    <dd class="mt-1 font-semibold">${escapeHtml(user.nickname)}</dd>
                </div>
            </dl>
        `;
    } catch (error) {
        clearTokens();
        updateAuthUi();
        panel.innerHTML = `<p class="text-error">${error.message}</p>`;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    updateAuthUi();
    setupLogin();
    setupRegister();
    setupLogout();
    setupBoardForm();
    setupPostForm();
    setupPostDelete();
    setupCommentForm();
    setupCommentDelete();
    setupMe();
});

# Simple Blog Spec

## 1. 목표

아주 간단한 게시판형 블로그 시스템을 만든다.
사용자는 회원가입/로그인 후 게시글과 댓글을 작성할 수 있다.

## 2. 주요 도메인

- User: 블로그 사용자
- Board: 게시판
- Post: 게시글
- Comment: 댓글
- RefreshToken: JWT 재발급용 인증 보조 정보

## 3. 사용자 기능

### 인증

- 회원가입
- 로그인
- 토큰 재발급
- 로그아웃

### 게시판

- 게시판 목록 조회
- 게시판별 게시글 목록 조회

### 게시글

- 게시글 작성
- 게시글 목록 조회
- 게시글 상세 조회
- 게시글 수정
- 게시글 삭제

### 댓글

- 댓글 작성
- 게시글별 댓글 목록 조회
- 댓글 수정
- 댓글 삭제

## 4. 권한 규칙

- 로그인한 사용자만 게시글을 작성할 수 있다.
- 게시글 작성자만 자신의 게시글을 수정/삭제할 수 있다.
- 로그인한 사용자만 댓글을 작성할 수 있다.
- 댓글 작성자만 자신의 댓글을 수정/삭제할 수 있다.
- 관리자는 모든 게시글과 댓글을 삭제할 수 있다.

## 5. 데이터 규칙

- `users.email`은 중복될 수 없다.
- `users.nickname`은 중복될 수 없다.
- `board.name`은 중복될 수 없다.
- 게시글은 반드시 하나의 사용자와 하나의 게시판에 속한다.
- 댓글은 반드시 하나의 게시글에 속한다.
- 게시글이 삭제되면 해당 댓글도 함께 삭제된다.
- 사용자가 삭제되면 해당 사용자의 리프레시 토큰도 삭제된다.

## 6. API 초안

### Auth

- `POST /api/auth/signup`
- `POST /api/auth/login`
- `POST /api/auth/refresh`
- `POST /api/auth/logout`

### Boards

- `GET /api/boards`
- `GET /api/boards/{boardId}/posts`

### Posts

- `POST /api/posts`
- `GET /api/posts`
- `GET /api/posts/{postId}`
- `PATCH /api/posts/{postId}`
- `DELETE /api/posts/{postId}`

### Comments

- `POST /api/posts/{postId}/comments`
- `GET /api/posts/{postId}/comments`
- `PATCH /api/comments/{commentId}`
- `DELETE /api/comments/{commentId}`
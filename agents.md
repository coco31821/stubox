# 에이전트 작업 메모

## 프로젝트

이 저장소는 아주 간단한 블로그 시스템을 만드는 Spring Boot + JPA 프로젝트다.
사용자가 명시적으로 요청하지 않는 한 도메인은 의도적으로 작게 유지한다.

핵심 블로그 테이블:

- `users`
- `board`
- `post`
- `comment`

인증 보조 테이블:

- `refresh_tokens`

블로그 도메인의 핵심은 네 테이블이지만, JWT 재발급 흐름을 구현할 때는 `refresh_tokens` 테이블을 유지한다.

## ERD 검토

제안된 네 테이블 구조는 간단한 블로그에 적절하다.

- 하나의 `board`는 여러 `post`를 가진다.
- 하나의 `users`는 여러 `post`를 가진다.
- 하나의 `post`는 여러 `comment`를 가진다.


권장 키 구조:

- `users.user_id`: 기본키, 자동 증가.
- `board.board_id`: 기본키, 자동 증가.
- `post.post_id`: 기본키, 자동 증가.
- `post.user_id`: `users.user_id`를 참조하는 외래키, 인덱스 필요.
- `post.board_id`: `board.board_id`를 참조하는 외래키, 인덱스 필요.
- `comment.comment_id`: 기본키, 자동 증가.
- `comment.post_id`: `post.post_id`를 참조하는 외래키, 인덱스 필요.
- `comment.user_id`: `users.user_id`를 참조하는 외래키, 인덱스 필요.
- `refresh_tokens.refresh_token_id`: 기본키, 자동 증가.
- `refresh_tokens.user_id`: `users.user_id`를 참조하는 외래키, 인덱스 필요.
- `refresh_tokens.token`: 토큰 문자열, 유니크 제약 필요.

댓글은 로그인 사용자와 연결하므로 `comment.user_id`를 두고 `users.user_id`를 참조하게 한다.
`comment.nickname`은 댓글 작성 당시의 표시 이름으로 함께 저장한다.
`refresh_tokens.user_id` 역시 외래키일 뿐이며 기본키나 `AUTO_INCREMENT`로 지정하지 않는다.

## 컬럼 기준

- 테이블명은 `user` 대신 `users`를 사용한다.
- `users.email`에는 유니크 제약을 둔다.
- `users.nickname`은 사용자 표시 이름으로 필수 컬럼이며, 유니크 제약을 둔다.
- 게시판 이름 중복을 막아야 한다면 `board.name`에도 유니크 제약을 둔다.
- 변경 가능한 테이블에는 `created_at`, `updated_at`을 일관되게 둔다.
- `created_at`은 `NOT NULL`로 둔다.
- `updated_at`은 "아직 수정된 적 없음"을 구분해야 할 때만 nullable로 둔다. 그렇지 않다면 `NOT NULL`로 두고 자동 갱신한다.
- 게시글과 댓글 본문은 `TEXT`를 사용한다.
- `users.password`에는 평문 비밀번호가 아니라 비밀번호 해시를 저장한다.
- `refresh_tokens.token`에는 유니크 제약을 둔다.
- `refresh_tokens.expired_at`은 토큰 만료 시각을 저장하며 `NOT NULL`로 둔다.
- 과거 닉네임 표시가 꼭 필요한 요구사항이 아니라면 `post`나 `comment`에 `nickname`을 중복 저장하지 않는다.

## 네이밍 기준

- Java 필드는 `userId`처럼 camelCase를 사용한다.
- 데이터베이스 컬럼은 `user_id`처럼 snake_case를 사용한다.
- 테이블명 규칙은 일관되게 유지한다. 현재 코드는 `users`를 사용하며, 새 블로그 테이블은 `board`, `post`, `comment`처럼 단수형을 써도 된다.
- 리프레시 토큰 테이블은 현재 엔티티와 맞춰 `refresh_tokens`를 사용한다.

## 구현 메모

- 기존 `io.coco318213.stubox` 패키지 스타일을 따른다.
- 타임스탬프 필드는 `BaseEntity`를 재사용한다.
- `ManyToOne` 참조는 지연 로딩을 우선 사용한다.
- 모든 외래키 컬럼에는 인덱스를 추가한다.
- 간단한 블로그를 구현하는 동안 관련 없는 도메인을 새로 추가하지 않는다.

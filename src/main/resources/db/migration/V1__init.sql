CREATE TABLE users (
    id          UUID PRIMARY KEY,
    name        VARCHAR(120) NOT NULL,
    user_type   VARCHAR(16)  NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE themes (
    id                      UUID PRIMARY KEY,
    title                   VARCHAR(160) NOT NULL,
    description             TEXT         NOT NULL,
    sort_order              INT          NOT NULL UNIQUE,
    checkpoint_pass_percent INT          NOT NULL DEFAULT 70
);

CREATE TABLE lessons (
    id           UUID PRIMARY KEY,
    theme_id     UUID         NOT NULL REFERENCES themes (id),
    title        VARCHAR(160) NOT NULL,
    description  TEXT         NOT NULL,
    video_url    VARCHAR(500) NOT NULL,
    sort_order   INT          NOT NULL,
    UNIQUE (theme_id, sort_order)
);

CREATE TABLE gestures (
    id          UUID PRIMARY KEY,
    name        VARCHAR(160) NOT NULL,
    description TEXT         NOT NULL,
    video_url   VARCHAR(500) NOT NULL
);

CREATE TABLE lesson_gestures (
    lesson_id   UUID NOT NULL REFERENCES lessons (id),
    gesture_id  UUID NOT NULL REFERENCES gestures (id),
    sort_order  INT  NOT NULL,
    PRIMARY KEY (lesson_id, gesture_id),
    UNIQUE (lesson_id, sort_order)
);

CREATE TABLE user_lesson_progress (
    user_id          UUID        NOT NULL REFERENCES users (id),
    lesson_id        UUID        NOT NULL REFERENCES lessons (id),
    video_watched_at TIMESTAMPTZ,
    completed_at     TIMESTAMPTZ,
    PRIMARY KEY (user_id, lesson_id)
);

CREATE TABLE practice_attempts (
    id            UUID PRIMARY KEY,
    user_id       UUID         NOT NULL REFERENCES users (id),
    lesson_id     UUID         NOT NULL REFERENCES lessons (id),
    gesture_id    UUID         NOT NULL REFERENCES gestures (id),
    recording_url VARCHAR(500) NOT NULL,
    success       BOOLEAN      NOT NULL,
    errors        JSONB        NOT NULL DEFAULT '[]',
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_practice_attempts_user_lesson
    ON practice_attempts (user_id, lesson_id, gesture_id);

CREATE TABLE checkpoints (
    id       UUID PRIMARY KEY,
    theme_id UUID         NOT NULL UNIQUE REFERENCES themes (id),
    title    VARCHAR(160) NOT NULL
);

CREATE TABLE checkpoint_questions (
    id            UUID PRIMARY KEY,
    checkpoint_id UUID         NOT NULL REFERENCES checkpoints (id),
    prompt        VARCHAR(400) NOT NULL,
    sort_order    INT          NOT NULL,
    UNIQUE (checkpoint_id, sort_order)
);

CREATE TABLE checkpoint_options (
    id          UUID PRIMARY KEY,
    question_id UUID    NOT NULL REFERENCES checkpoint_questions (id),
    gesture_id  UUID    NOT NULL REFERENCES gestures (id),
    is_correct  BOOLEAN NOT NULL,
    sort_order  INT     NOT NULL,
    UNIQUE (question_id, sort_order)
);

CREATE TABLE checkpoint_attempts (
    id            UUID PRIMARY KEY,
    user_id       UUID        NOT NULL REFERENCES users (id),
    checkpoint_id UUID        NOT NULL REFERENCES checkpoints (id),
    passed        BOOLEAN     NOT NULL,
    score_percent INT         NOT NULL,
    answers       JSONB       NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_checkpoint_attempts_user
    ON checkpoint_attempts (user_id, checkpoint_id, created_at DESC);

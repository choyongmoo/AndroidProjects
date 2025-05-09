-- PostgreSQL

DROP TYPE IF EXISTS friend_status CASCADE;

DROP TABLE IF EXISTS sample CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS places CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS arrival_logs CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS group_members CASCADE;
DROP TABLE IF EXISTS group_places CASCADE;
DROP TABLE IF EXISTS live_locations CASCADE;
DROP TABLE IF EXISTS penalties CASCADE;

-- ENUM: friend_status
CREATE TYPE friend_status AS ENUM ('pending', 'accepted', 'blocked');

-- TABLE: sample
CREATE TABLE sample (
    id      BIGSERIAL    PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);

-- TABLE: users
CREATE TABLE users (
    id            BIGSERIAL              NOT NULL PRIMARY KEY,
    username      CHARACTER VARYING(100) NOT NULL UNIQUE,
    email         CHARACTER VARYING(100) NOT NULL UNIQUE,
    password_hash CHARACTER VARYING(255) NOT NULL,
    display_name  CHARACTER VARYING(100) NOT NULL,
    created_at    TIMESTAMP DEFAULT NOW()
);

-- TABLE: places
CREATE TABLE places (
    id      BIGSERIAL              NOT NULL PRIMARY KEY,
    name    CHARACTER VARYING(200) NOT NULL,
    lat     DOUBLE PRECISION       NOT NULL,
    lng     DOUBLE PRECISION       NOT NULL,
    address CHARACTER VARYING(300)
);

-- TABLE: groups
CREATE TABLE groups (
    id          BIGSERIAL              NOT NULL PRIMARY KEY,
    group_name  CHARACTER VARYING(100) NOT NULL,
    created_by  BIGINT                 NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_created_by_users FOREIGN KEY (created_by) REFERENCES users (id)
);

-- TABLE: appointments
CREATE TABLE appointments (
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    group_id   BIGINT    NOT NULL,
    place_id   BIGINT    NOT NULL,
    "time"     TIMESTAMP NOT NULL,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_appointments_groups   FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT fk_appointments_places   FOREIGN KEY (place_id) REFERENCES places (id),
    CONSTRAINT fk_appointments_users    FOREIGN KEY (created_by) REFERENCES users (id)
);

-- TABLE: arrival_logs
CREATE TABLE arrival_logs (
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    appointment_id BIGINT    NOT NULL,
    user_id        BIGINT    NOT NULL,
    arrival_time   TIMESTAMP NOT NULL,
    is_late        BOOLEAN,
    CONSTRAINT unique_arrival               UNIQUE (appointment_id, user_id),
    CONSTRAINT fk_arrival_logs_appointments FOREIGN KEY (appointment_id) REFERENCES appointments (id),
    CONSTRAINT fk_arrival_logs_users        FOREIGN KEY (user_id) REFERENCES users (id)
);

-- TABLE: friends
CREATE TABLE friends (
    id         BIGSERIAL       NOT NULL PRIMARY KEY,
    user_id    BIGINT          NOT NULL,
    friend_id  BIGINT          NOT NULL,
    created_at TIMESTAMP,
    status     friend_status   NOT NULL DEFAULT 'pending'::friend_status,
    CONSTRAINT unique_friend_pair     UNIQUE (user_id, friend_id),
    CONSTRAINT fk_created_by_user     FOREIGN KEY (user_id)   REFERENCES users (id) NOT VALID,
    CONSTRAINT fk_created_by_friend   FOREIGN KEY (friend_id) REFERENCES users (id) NOT VALID
);

-- TABLE: group_members
CREATE TABLE group_members (
    id        BIGSERIAL NOT NULL PRIMARY KEY,
    group_id  BIGINT    NOT NULL,
    user_id   BIGINT    NOT NULL,
    CONSTRAINT unique_group_member       UNIQUE (group_id, user_id),
    CONSTRAINT fk_group_members_groups   FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT fk_group_members_users    FOREIGN KEY (user_id)  REFERENCES users (id) NOT VALID
);

-- TABLE: group_places
CREATE TABLE group_places (
    id        BIGSERIAL NOT NULL PRIMARY KEY,
    group_id  BIGINT    NOT NULL,
    place_id  BIGINT    NOT NULL,
    CONSTRAINT unique_group_place       UNIQUE (group_id, place_id),
    CONSTRAINT fk_group_places_groups   FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT fk_group_places_places   FOREIGN KEY (place_id) REFERENCES places (id)
);

-- TABLE: live_locations
CREATE TABLE live_locations (
    id         BIGSERIAL        NOT NULL PRIMARY KEY,
    user_id    BIGINT           NOT NULL UNIQUE,
    lat        DOUBLE PRECISION NOT NULL,
    lng        DOUBLE PRECISION NOT NULL,
    update_at  TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_live_locations_users FOREIGN KEY (user_id) REFERENCES users (id)
);

-- TABLE: penalties
CREATE TABLE penalties (
    id             BIGSERIAL NOT NULL PRIMARY KEY,
    appointment_id BIGINT    NOT NULL,
    payer_id       BIGINT    NOT NULL,
    amount         INTEGER   NOT NULL,
    CONSTRAINT fk_penalties_appointments FOREIGN KEY (appointment_id) REFERENCES appointments (id),
    CONSTRAINT fk_penalties_users        FOREIGN KEY (payer_id)       REFERENCES users (id)
);

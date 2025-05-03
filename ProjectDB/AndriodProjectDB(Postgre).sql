
-- DROP TABLES (기존 데이터 정리)
DROP TABLE IF EXISTS public.penalties CASCADE;
DROP TABLE IF EXISTS public.live_locations CASCADE;
DROP TABLE IF EXISTS public.group_places CASCADE;
DROP TABLE IF EXISTS public.group_members CASCADE;
DROP TABLE IF EXISTS public.friends CASCADE;
DROP TABLE IF EXISTS public.arrival_logs CASCADE;
DROP TABLE IF EXISTS public.appointments CASCADE;
DROP TABLE IF EXISTS public.groups CASCADE;
DROP TABLE IF EXISTS public.places CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;

-- DROP SEQUENCES (기존 시퀀스 정리)
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS places_id_seq CASCADE;
DROP SEQUENCE IF EXISTS groups_id_seq CASCADE;
DROP SEQUENCE IF EXISTS appointments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS appointments_group_id_seq CASCADE;
DROP SEQUENCE IF EXISTS appointments_place_id_seq CASCADE;
DROP SEQUENCE IF EXISTS arrival_logs_id_seq CASCADE;
DROP SEQUENCE IF EXISTS friends_id_seq CASCADE;
DROP SEQUENCE IF EXISTS group_members_id_seq CASCADE;
DROP SEQUENCE IF EXISTS group_places_id_seq CASCADE;
DROP SEQUENCE IF EXISTS live_locations_id_seq CASCADE;
DROP SEQUENCE IF EXISTS penalties_id_seq CASCADE;

-- CREATE SEQUENCES
CREATE SEQUENCE users_id_seq START 1;
CREATE SEQUENCE places_id_seq START 1;
CREATE SEQUENCE groups_id_seq START 1;
CREATE SEQUENCE appointments_id_seq START 1;
CREATE SEQUENCE appointments_group_id_seq START 1;
CREATE SEQUENCE appointments_place_id_seq START 1;
CREATE SEQUENCE arrival_logs_id_seq START 1;
CREATE SEQUENCE friends_id_seq START 1;
CREATE SEQUENCE group_members_id_seq START 1;
CREATE SEQUENCE group_places_id_seq START 1;
CREATE SEQUENCE live_locations_id_seq START 1;
CREATE SEQUENCE penalties_id_seq START 1;

-- 이하 테이블 생성 (당신이 작성한 것 그대로 사용)

-- Table: users
CREATE TABLE IF NOT EXISTS public.users (
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    user_id character varying(50) NOT NULL,
    username character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp,
    update_at timestamp,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT user_id_unique UNIQUE (user_id)
);

ALTER TABLE public.users OWNER TO postgres;

-- Table: places
CREATE TABLE IF NOT EXISTS public.places (
    id bigint NOT NULL DEFAULT nextval('places_id_seq'::regclass),
    name character varying(200) NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    address character varying(300),
    CONSTRAINT places_pkey PRIMARY KEY (id)
);

ALTER TABLE public.places OWNER TO postgres;

-- Table: groups
CREATE TABLE IF NOT EXISTS public.groups (
    id bigint NOT NULL DEFAULT nextval('groups_id_seq'::regclass),
    group_name character varying(100) NOT NULL,
    created_by bigint NOT NULL,
    created_at timestamp DEFAULT now(),
    CONSTRAINT groups_pkey PRIMARY KEY (id),
    CONSTRAINT fk_created_by_users FOREIGN KEY (created_by) REFERENCES public.users (id)
);

ALTER TABLE public.groups OWNER TO postgres;

-- Table: appointments
CREATE TABLE IF NOT EXISTS public.appointments (
    id bigint NOT NULL DEFAULT nextval('appointments_id_seq'::regclass),
    group_id bigint NOT NULL DEFAULT nextval('appointments_group_id_seq'::regclass),
    place_id bigint NOT NULL DEFAULT nextval('appointments_place_id_seq'::regclass),
    "time" timestamp NOT NULL,
    created_by bigint,
    created_at timestamp DEFAULT now(),
    CONSTRAINT appointments_pkey PRIMARY KEY (id),
    CONSTRAINT fk_appointments_groups FOREIGN KEY (group_id) REFERENCES public.groups (id),
    CONSTRAINT fk_appointments_places FOREIGN KEY (place_id) REFERENCES public.places (id),
    CONSTRAINT fk_appointments_users FOREIGN KEY (created_by) REFERENCES public.users (id)
);

ALTER TABLE public.appointments OWNER TO postgres;

-- Table: arrival_logs
CREATE TABLE IF NOT EXISTS public.arrival_logs (
    id bigint NOT NULL DEFAULT nextval('arrival_logs_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    user_id bigint NOT NULL,
    arrival_time timestamp NOT NULL,
    is_late boolean,
    CONSTRAINT arrival_logs_pkey PRIMARY KEY (id),
    CONSTRAINT unique_arrival UNIQUE (appointment_id, user_id),
    CONSTRAINT fk_arrival_logs_appointments FOREIGN KEY (appointment_id) REFERENCES public.appointments (id),
    CONSTRAINT fk_arrival_logs_users FOREIGN KEY (user_id) REFERENCES public.users (id)
);

ALTER TABLE public.arrival_logs OWNER TO postgres;

-- Table: friends
CREATE TABLE IF NOT EXISTS public.friends (
    id bigint NOT NULL DEFAULT nextval('friends_id_seq'::regclass),
    user_id bigint NOT NULL,
    friend_id bigint NOT NULL,
    created_at timestamp,
    status friend_status NOT NULL DEFAULT 'pending'::friend_status,
    CONSTRAINT friends_pkey PRIMARY KEY (id),
    CONSTRAINT unique_friend_pair UNIQUE (user_id, friend_id),
    CONSTRAINT fk_created_by_user FOREIGN KEY (user_id) REFERENCES public.users (id) NOT VALID,
    CONSTRAINT fk_created_by_friend FOREIGN KEY (friend_id) REFERENCES public.users (id) NOT VALID
);

ALTER TABLE public.friends OWNER TO postgres;

-- Table: group_members
CREATE TABLE IF NOT EXISTS public.group_members (
    id bigint NOT NULL DEFAULT nextval('group_members_id_seq'::regclass),
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT group_members_pkey PRIMARY KEY (id),
    CONSTRAINT unique_group_member UNIQUE (group_id, user_id),
    CONSTRAINT fk_group_members_groups FOREIGN KEY (group_id) REFERENCES public.groups (id),
    CONSTRAINT fk_group_members_users FOREIGN KEY (user_id) REFERENCES public.users (id) NOT VALID
);

ALTER TABLE public.group_members OWNER TO postgres;

-- Table: group_places
CREATE TABLE IF NOT EXISTS public.group_places (
    id bigint NOT NULL DEFAULT nextval('group_places_id_seq'::regclass),
    group_id bigint NOT NULL,
    place_id bigint NOT NULL,
    CONSTRAINT group_places_pkey PRIMARY KEY (id),
    CONSTRAINT unique_group_place UNIQUE (group_id, place_id),
    CONSTRAINT fk_group_places_groups FOREIGN KEY (group_id) REFERENCES public.groups (id),
    CONSTRAINT fk_group_places_places FOREIGN KEY (place_id) REFERENCES public.places (id)
);

ALTER TABLE public.group_places OWNER TO postgres;

-- Table: live_locations
CREATE TABLE IF NOT EXISTS public.live_locations (
    id bigint NOT NULL DEFAULT nextval('live_locations_id_seq'::regclass),
    user_id bigint NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    update_at timestamp DEFAULT now(),
    CONSTRAINT live_locations_pkey PRIMARY KEY (id),
    CONSTRAINT unique_live_location UNIQUE (user_id),
    CONSTRAINT fk_live_locations_users FOREIGN KEY (user_id) REFERENCES public.users (id)
);

ALTER TABLE public.live_locations OWNER TO postgres;

-- Table: penalties
CREATE TABLE IF NOT EXISTS public.penalties (
    id bigint NOT NULL DEFAULT nextval('penalties_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    payer_id bigint NOT NULL,
    amount integer NOT NULL,
    receiver_ids json NOT NULL,
    CONSTRAINT penalties_pkey PRIMARY KEY (id),
    CONSTRAINT fk_penalties_appointments FOREIGN KEY (appointment_id) REFERENCES public.appointments (id),
    CONSTRAINT fk_penalties_users FOREIGN KEY (payer_id) REFERENCES public.users (id)
);

ALTER TABLE public.penalties OWNER TO postgres;

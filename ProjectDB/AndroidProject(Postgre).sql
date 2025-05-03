DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.places CASCADE;
DROP TABLE IF EXISTS public.groups CASCADE;
DROP TABLE IF EXISTS public.appointments CASCADE;
DROP TABLE IF EXISTS public.arrival_logs CASCADE;
DROP TABLE IF EXISTS public.friends CASCADE;
DROP TABLE IF EXISTS public.group_members CASCADE;
DROP TABLE IF EXISTS public.group_places CASCADE;
DROP TABLE IF EXISTS public.live_locations CASCADE;
DROP TABLE IF EXISTS public.penalties CASCADE;

-- Create Tables in Dependency Order

-- Table: users
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    user_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    username character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone,
    update_at timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT email_unique UNIQUE (email),
    CONSTRAINT user_id_unique UNIQUE (user_id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

COMMENT ON CONSTRAINT email_unique ON public.users
    IS 'email 유니크 키';
COMMENT ON CONSTRAINT user_id_unique ON public.users
    IS 'user_id 유니크 키';

-- Table: places
CREATE TABLE IF NOT EXISTS public.places
(
    id bigint NOT NULL DEFAULT nextval('places_id_seq'::regclass),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    address character varying(300) COLLATE pg_catalog."default",
    CONSTRAINT places_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.places
    OWNER to postgres;

COMMENT ON TABLE public.places
    IS '장소 즐겨찾기';

-- Table: groups
CREATE TABLE IF NOT EXISTS public.groups
(
    id bigint NOT NULL DEFAULT nextval('groups_id_seq'::regclass),
    group_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    created_by bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT groups_pkey PRIMARY KEY (id),
    CONSTRAINT fk_created_by_users FOREIGN KEY (created_by)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.groups
    OWNER to postgres;

COMMENT ON TABLE public.groups
    IS '그룹 자체 정보';

-- Table: appointments
CREATE TABLE IF NOT EXISTS public.appointments
(
    id bigint NOT NULL DEFAULT nextval('appointments_id_seq'::regclass),
    group_id bigint NOT NULL DEFAULT nextval('appointments_group_id_seq'::regclass),
    place_id bigint NOT NULL DEFAULT nextval('appointments_place_id_seq'::regclass),
    "time" timestamp without time zone NOT NULL,
    created_by bigint,
    created_at timestamp without time zone DEFAULT now(),
    CONSTRAINT appointments_pkey PRIMARY KEY (id),
    CONSTRAINT fk_appointments_groups FOREIGN KEY (group_id)
        REFERENCES public.groups (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_appointments_places FOREIGN KEY (place_id)
        REFERENCES public.places (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_appointments_users FOREIGN KEY (created_by)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.appointments
    OWNER to postgres;

COMMENT ON TABLE public.appointments
    IS '약속';

-- Table: arrival_logs
CREATE TABLE IF NOT EXISTS public.arrival_logs
(
    id bigint NOT NULL DEFAULT nextval('arrival_logs_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    user_id bigint NOT NULL,
    arrival_time timestamp without time zone NOT NULL,
    is_late boolean,
    CONSTRAINT arrival_logs_pkey PRIMARY KEY (id),
    CONSTRAINT unique_arrival UNIQUE (appointment_id, user_id),
    CONSTRAINT fk_arrival_logs_appointments FOREIGN KEY (appointment_id)
        REFERENCES public.appointments (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_arrival_logs_users FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.arrival_logs
    OWNER to postgres;

COMMENT ON TABLE public.arrival_logs
    IS '도착 로그';

-- Table: friends
CREATE TABLE IF NOT EXISTS public.friends
(
    id bigint NOT NULL DEFAULT nextval('friends_id_seq'::regclass),
    user_id bigint NOT NULL,
    friend_id bigint NOT NULL,
    created_at timestamp without time zone,
    status friend_status NOT NULL DEFAULT 'pending'::friend_status,
    CONSTRAINT friends_pkey PRIMARY KEY (id),
    CONSTRAINT unique_friend_pair UNIQUE (user_id, friend_id),
    CONSTRAINT fk_created_by_friend FOREIGN KEY (friend_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_created_by_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.friends
    OWNER to postgres;

COMMENT ON CONSTRAINT unique_friend_pair ON public.friends
    IS '중복 등록 방지';

-- Table: group_members
CREATE TABLE IF NOT EXISTS public.group_members
(
    id bigint NOT NULL DEFAULT nextval('group_members_id_seq'::regclass),
    group_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT group_members_pkey PRIMARY KEY (id),
    CONSTRAINT unique_group_member UNIQUE (group_id, user_id),
    CONSTRAINT fk_group_members_groups FOREIGN KEY (group_id)
        REFERENCES public.groups (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_group_members_users FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.group_members
    OWNER to postgres;

-- Table: group_places
CREATE TABLE IF NOT EXISTS public.group_places
(
    id bigint NOT NULL DEFAULT nextval('group_places_id_seq'::regclass),
    group_id bigint NOT NULL,
    place_id bigint NOT NULL,
    CONSTRAINT group_places_pkey PRIMARY KEY (id),
    CONSTRAINT unique_group_place UNIQUE (group_id, place_id),
    CONSTRAINT fk_group_places_groups FOREIGN KEY (group_id)
        REFERENCES public.groups (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_group_places_places FOREIGN KEY (place_id)
        REFERENCES public.places (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.group_places
    OWNER to postgres;

COMMENT ON TABLE public.group_places
    IS '그룹별 즐겨찾기 장소';

-- Table: live_locations
CREATE TABLE IF NOT EXISTS public.live_locations
(
    id bigint NOT NULL DEFAULT nextval('live_locations_id_seq'::regclass),
    user_id bigint NOT NULL,
    lat double precision NOT NULL,
    lng double precision NOT NULL,
    update_at timestamp without time zone DEFAULT now(),
    CONSTRAINT live_locations_pkey PRIMARY KEY (id),
    CONSTRAINT unique_live_location UNIQUE (user_id),
    CONSTRAINT fk_live_locations_users FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.live_locations
    OWNER to postgres;

COMMENT ON TABLE public.live_locations
    IS '실시간 위치';

-- Table: penalties
CREATE TABLE IF NOT EXISTS public.penalties
(
    id bigint NOT NULL DEFAULT nextval('penalties_id_seq'::regclass),
    appointment_id bigint NOT NULL,
    payer_id bigint NOT NULL,
    amount integer NOT NULL,
    receiver_ids json NOT NULL,
    CONSTRAINT penalties_pkey PRIMARY KEY (id),
    CONSTRAINT fk_penalties_appointments FOREIGN KEY (appointment_id)
        REFERENCES public.appointments (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_penalties_users FOREIGN KEY (payer_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.penalties
    OWNER to postgres;

COMMENT ON TABLE public.penalties
    IS '벌금 기록';


CREATE TABLE public.areas
(
    id          serial primary key,
    description text,
    name        text
);


CREATE TABLE public.statuses
(
    id               serial primary key,
    body             jsonb,
    creationDate     timestamp without time zone,
    modificationDate timestamp without time zone
);

CREATE TABLE public.users
(
    id        serial primary key,
    createdAt timestamp without time zone,
    name      text,
    role      text,
    password  text,
    username  text UNIQUE
);

CREATE TABLE public.incidents
(
    id          serial primary key,
    closed      boolean not null,
    description text,
    icon        text,
    name        text,
    priority    text,
    due         timestamp without time zone,
    start       timestamp without time zone,
    area_id     bigint,
    assignee_id bigint,
    status_id   bigint,

    FOREIGN KEY (assignee_id) REFERENCES public.users (id),
    FOREIGN KEY (status_id) REFERENCES public.statuses (id),
    FOREIGN KEY (area_id) REFERENCES public.areas (id)
);


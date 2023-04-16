create table if not exists aircraft
(
    id bigserial not null,
    category character varying(255),
    model character varying(255),
    constraint aircraft_pk primary key (id)
)
-- tablespace pg_default;

-- alter table aircraft owner to postgres;

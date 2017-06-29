
-- Table: tipo_habitacion

-- DROP TABLE tipo_habitacion;
CREATE SEQUENCE reserva_id_reserva_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE reserva_id_reserva_seq
    OWNER TO postgres;
CREATE TABLE tipo_habitacion
(
    id_tipo_habitacion integer NOT NULL,
    numero_camas integer NOT NULL,
    costo numeric(1000, 2) NOT NULL,
    descripcion character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_tipo_habitacion PRIMARY KEY (id_tipo_habitacion)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE tipo_habitacion
    OWNER to postgres;

-- Index: tipo_habitacion_pk

-- DROP INDEX tipo_habitacion_pk;

CREATE UNIQUE INDEX tipo_habitacion_pk
    ON tipo_habitacion USING btree
    (id_tipo_habitacion)
    TABLESPACE pg_default;

-- Table: extra_habitacion

-- DROP TABLE extra_habitacion;

CREATE TABLE extra_habitacion
(
    id_extra_habitacion integer NOT NULL,
    jacuzzi boolean,
    vista_al_mar boolean,
    costo_total numeric(1000, 2) NOT NULL,
    CONSTRAINT pk_extra_habitacion PRIMARY KEY (id_extra_habitacion)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE extra_habitacion
    OWNER to postgres;

-- Index: extra_habitacion_pk

-- DROP INDEX extra_habitacion_pk;

CREATE UNIQUE INDEX extra_habitacion_pk
    ON extra_habitacion USING btree
    (id_extra_habitacion)
    TABLESPACE pg_default;




    -- Table: habitacion

-- DROP TABLE habitacion;

CREATE TABLE habitacion
(
    id_habitacion integer NOT NULL,
    id_extra_habitacion integer,
    id_tipo_habitacion integer NOT NULL,
    numero_piso integer NOT NULL,
    CONSTRAINT pk_habitacion PRIMARY KEY (id_habitacion),
    CONSTRAINT fk_habitaci_habitacio_extra_ha FOREIGN KEY (id_extra_habitacion)
        REFERENCES extra_habitacion (id_extra_habitacion) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_habitaci_habitacio_tipo_hab FOREIGN KEY (id_tipo_habitacion)
        REFERENCES tipo_habitacion (id_tipo_habitacion) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE habitacion
    OWNER to postgres;

-- Index: habitacion_extra_habitacion_fk

-- DROP INDEX habitacion_extra_habitacion_fk;

CREATE INDEX habitacion_extra_habitacion_fk
    ON habitacion USING btree
    (id_extra_habitacion)
    TABLESPACE pg_default;

-- Index: habitacion_pk

-- DROP INDEX habitacion_pk;

CREATE UNIQUE INDEX habitacion_pk
    ON habitacion USING btree
    (id_habitacion)
    TABLESPACE pg_default;

-- Index: habitacion_tipo_ha_fk

-- DROP INDEX habitacion_tipo_ha_fk;

CREATE INDEX habitacion_tipo_ha_fk
    ON habitacion USING btree
    (id_tipo_habitacion)
    TABLESPACE pg_default;


    -- Table: huesped

-- DROP TABLE huesped;

CREATE TABLE huesped
(
    id_huesped character varying(10) COLLATE pg_catalog."default" NOT NULL,
    nombre_huesped character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_huesped PRIMARY KEY (id_huesped)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;



-- Index: huesped_pk

-- DROP INDEX huesped_pk;

CREATE UNIQUE INDEX huesped_pk
    ON huesped USING btree
    (id_huesped COLLATE pg_catalog."default")
    TABLESPACE pg_default;



    -- Table: ha_huesped

-- DROP TABLE ha_huesped;

CREATE TABLE ha_huesped
(
    fecha_habitacion_huesped date NOT NULL,
    id_habitacion integer NOT NULL,
    id_huesped character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_ha_huesped PRIMARY KEY (fecha_habitacion_huesped, id_habitacion, id_huesped),
    CONSTRAINT fk_ha_huesp_ha_huespe_huesped FOREIGN KEY (id_huesped)
        REFERENCES huesped (id_huesped) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_ha_huesp_habitacio_habitaci FOREIGN KEY (id_habitacion)
        REFERENCES habitacion (id_habitacion) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE ha_huesped
    OWNER to postgres;

-- Index: ha_huesped_huesped_fk

-- DROP INDEX ha_huesped_huesped_fk;

CREATE INDEX ha_huesped_huesped_fk
    ON ha_huesped USING btree
    (id_huesped COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: ha_huesped_pk

-- DROP INDEX ha_huesped_pk;

CREATE UNIQUE INDEX ha_huesped_pk
    ON ha_huesped USING btree
    (fecha_habitacion_huesped, id_habitacion, id_huesped COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- Index: habitacion_ha_huesped_fk

-- DROP INDEX habitacion_ha_huesped_fk;

CREATE INDEX habitacion_ha_huesped_fk
    ON ha_huesped USING btree
    (id_habitacion)
    TABLESPACE pg_default;


    -- Table: menu

-- DROP TABLE menu;

CREATE TABLE menu
(
    id_menu integer NOT NULL,
    desc_menu character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_menu PRIMARY KEY (id_menu)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE menu
    OWNER to postgres;

-- Index: menu_pk

-- DROP INDEX menu_pk;

CREATE UNIQUE INDEX menu_pk
    ON menu USING btree
    (id_menu)
    TABLESPACE pg_default;



    -- Table: perfil

-- DROP TABLE perfil;

CREATE TABLE perfil
(
    id_perfil integer NOT NULL,
    id_menu integer NOT NULL,
    desc_perfil character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_perfil PRIMARY KEY (id_perfil),
    CONSTRAINT fk_perfil_perfil_me_menu FOREIGN KEY (id_menu)
        REFERENCES menu (id_menu) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE perfil
    OWNER to postgres;

-- Index: perfil_menu_fk

-- DROP INDEX perfil_menu_fk;

CREATE INDEX perfil_menu_fk
    ON perfil USING btree
    (id_menu)
    TABLESPACE pg_default;

-- Index: perfil_pk

-- DROP INDEX perfil_pk;

CREATE UNIQUE INDEX perfil_pk
    ON perfil USING btree
    (id_perfil)
    TABLESPACE pg_default;

-- Table: servicio

-- DROP TABLE servicio;

CREATE TABLE servicio
(
    id_servicio integer NOT NULL,
    desayuno boolean NOT NULL,
    parqueadero boolean NOT NULL,
    costo_total numeric(1000, 2) NOT NULL,
    CONSTRAINT pk_servicio PRIMARY KEY (id_servicio)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE servicio
    OWNER to postgres;

-- Index: servicio_pk

-- DROP INDEX servicio_pk;

CREATE UNIQUE INDEX servicio_pk
    ON servicio USING btree
    (id_servicio)
    TABLESPACE pg_default;


    
    -- Table: usuario

-- DROP TABLE usuario;

CREATE TABLE usuario
(
    id_usuario integer NOT NULL,
    id_perfil integer NOT NULL,
    nombre_usuario character varying(100) COLLATE pg_catalog."default" NOT NULL,
    correo character varying(50) COLLATE pg_catalog."default",
    direccion character varying(100) COLLATE pg_catalog."default",
    telefono character varying(13) COLLATE pg_catalog."default",
    contrasenia character varying(10) COLLATE pg_catalog."default",
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT fk_usuario_usuario_p_perfil FOREIGN KEY (id_perfil)
        REFERENCES perfil (id_perfil) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE usuario
    OWNER to postgres;

-- Index: usuario_perfil_fk

-- DROP INDEX usuario_perfil_fk;

CREATE INDEX usuario_perfil_fk
    ON usuario USING btree
    (id_perfil)
    TABLESPACE pg_default;

-- Index: usuario_pk

-- DROP INDEX usuario_pk;

CREATE UNIQUE INDEX usuario_pk
    ON usuario USING btree
    (id_usuario)
    TABLESPACE pg_default;

 -- Table: reserva

-- DROP TABLE reserva;

CREATE TABLE reserva
(
    id_reserva integer NOT NULL DEFAULT nextval('reserva_id_reserva_seq'::regclass),
    id_usuario integer NOT NULL,
    id_servicio integer NOT NULL,
    fecha_inicio date NOT NULL,
    fecha_fin date NOT NULL,
    costo_total numeric(1000, 2) NOT NULL,
    numero_personas integer NOT NULL,
    CONSTRAINT pk_reserva PRIMARY KEY (id_reserva),
    CONSTRAINT fk_reserva_reserva_s_servicio FOREIGN KEY (id_servicio)
        REFERENCES servicio (id_servicio) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_usuario_r_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE reserva
    OWNER to postgres;

-- Index: reserva_pk

-- DROP INDEX reserva_pk;

CREATE UNIQUE INDEX reserva_pk
    ON reserva USING btree
    (id_reserva)
    TABLESPACE pg_default;

-- Index: reserva_servicio_fk

-- DROP INDEX reserva_servicio_fk;

CREATE INDEX reserva_servicio_fk
    ON reserva USING btree
    (id_servicio)
    TABLESPACE pg_default;

-- Index: usuario_reserva_fk

-- DROP INDEX usuario_reserva_fk;

CREATE INDEX usuario_reserva_fk
    ON reserva USING btree
    (id_usuario)
    TABLESPACE pg_default;



    -- Table: re_habitacion

-- DROP TABLE re_habitacion;

CREATE TABLE re_habitacion
(
    fecha_reserva_habitacion date NOT NULL,
    id_habitacion integer NOT NULL,
    id_reserva integer NOT NULL,
    CONSTRAINT pk_re_habitacion PRIMARY KEY (fecha_reserva_habitacion, id_reserva, id_habitacion),
    CONSTRAINT fk_re_habit_re_habita_habitaci FOREIGN KEY (id_habitacion)
        REFERENCES habitacion (id_habitacion) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT,
    CONSTRAINT fk_re_habit_reservas__reserva FOREIGN KEY (id_reserva)
        REFERENCES reserva (id_reserva) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE re_habitacion
    OWNER to postgres;

-- Index: re_habitacion_habitacion_fk

-- DROP INDEX re_habitacion_habitacion_fk;

CREATE INDEX re_habitacion_habitacion_fk
    ON re_habitacion USING btree
    (id_habitacion)
    TABLESPACE pg_default;

-- Index: re_habitacion_pk

-- DROP INDEX re_habitacion_pk;

CREATE UNIQUE INDEX re_habitacion_pk
    ON re_habitacion USING btree
    (fecha_reserva_habitacion, id_habitacion, id_reserva)
    TABLESPACE pg_default;

-- Index: reservas_re_habitacion_fk

-- DROP INDEX reservas_re_habitacion_fk;

CREATE INDEX reservas_re_habitacion_fk
    ON re_habitacion USING btree
    (id_reserva)
    TABLESPACE pg_default;


   


    
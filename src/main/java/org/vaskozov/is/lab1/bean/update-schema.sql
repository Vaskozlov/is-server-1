CREATE TABLE Location
(
    id   BIGINT           NOT NULL,
    x    DOUBLE PRECISION NOT NULL,
    y    DOUBLE PRECISION NOT NULL,
    name VARCHAR(255)     NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

DROP SEQUENCE coordinates_seq CASCADE;
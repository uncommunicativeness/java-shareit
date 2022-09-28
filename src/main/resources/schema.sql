DROP TABLE if EXISTS bookings;
DROP TABLE if EXISTS comments;
DROP TABLE if EXISTS item;
DROP TABLE if EXISTS item_requests;
DROP TABLE if EXISTS users;

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users
(
    id    bigint       NOT NULL,
    name  varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

CREATE SEQUENCE item_requests_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE item_requests
(
    id           bigint NOT NULL,
    description  varchar(10000),
    created      timestamp,
    requestor_id bigint,
    CONSTRAINT pk_item_requests PRIMARY KEY (id)
);

ALTER TABLE item_requests
    ADD CONSTRAINT fk_item_requests_on_requestor FOREIGN KEY (requestor_id) REFERENCES users (id);

CREATE SEQUENCE items_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE items
(
    id           bigint         NOT NULL,
    name         varchar(255)   NOT NULL,
    description  varchar(10000) NOT NULL,
    is_available boolean        NOT NULL,
    owner_id     bigint         NOT NULL,
    request_id   bigint,
    CONSTRAINT pk_items PRIMARY KEY (id)
);

ALTER TABLE items
    ADD CONSTRAINT fk_items_on_owner FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE items
    ADD CONSTRAINT fk_items_on_request FOREIGN KEY (request_id) REFERENCES item_requests (id);

CREATE SEQUENCE bookings_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE bookings
(
    id         bigint NOT NULL,
    start_date timestamp,
    end_date   timestamp,
    status     varchar(255),
    item_id    bigint,
    booker_id  bigint,
    CONSTRAINT pk_bookings PRIMARY KEY (id)
);

ALTER TABLE bookings
    ADD CONSTRAINT fk_bookings_on_booker FOREIGN KEY (booker_id) REFERENCES users (id);

ALTER TABLE bookings
    ADD CONSTRAINT fk_bookings_on_item FOREIGN KEY (item_id) REFERENCES items (id);

CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE comments
(
    id        bigint         NOT NULL,
    text      varchar(10000) NOT NULL,
    created   timestamp,
    item_id   bigint,
    author_id bigint,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_on_author FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_on_item FOREIGN KEY (item_id) REFERENCES items (id);


CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS item_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR                                 NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    requestor_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT pk_item_request PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    available   BOOLEAN                                 NOT NULL DEFAULT FALSE,
    owner_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    request_id  BIGINT REFERENCES item_requests (id) ON DELETE CASCADE,
    CONSTRAINT pk_item PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status     VARCHAR(30)                             NOT NULL,
    booker_id  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    item_id    BIGINT REFERENCES items (id) ON DELETE CASCADE,
    CONSTRAINT pk_booking PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text         VARCHAR                                 NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id      BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT pk_comment PRIMARY KEY (id)
    );
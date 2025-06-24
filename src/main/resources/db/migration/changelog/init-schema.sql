DROP TYPE IF EXISTS card_status;
CREATE TYPE card_status AS ENUM ('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE bank_user (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    username VARCHAR(30)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE bank_card (
    id              BIGSERIAL PRIMARY KEY,
    card_num        VARCHAR(19) NOT NULL,
    bank_user_id    BIGINT NOT NULL REFERENCES bank_user (id),
    expiration_date DATE NOT NULL,
    status          VARCHAR NOT NULL,
    balance         DECIMAL NOT NULL,
    CONSTRAINT non_negative_balance CHECK (balance >= 0)
);
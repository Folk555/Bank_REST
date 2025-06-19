CREATE TABLE bank_user (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL
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

CREATE TYPE card_status AS ENUM ('ACTIVE', 'BLOCKED', 'EXPIRED');
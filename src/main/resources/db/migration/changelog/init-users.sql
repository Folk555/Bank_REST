-- changeset author:folk
INSERT INTO bank_user (name, username, password, role, enabled)
VALUES
    ('Alice', 'alice', '$2a$10$kQEHTEtDudP0nblSHSZXy.IIDhZBX9P9VbCzKL8wkEXSq9bB6BYsC', 'USER', true),
    ('Bob', 'bob', '$2a$10$kQEHTEtDudP0nblSHSZXy.IIDhZBX9P9VbCzKL8wkEXSq9bB6BYsC', 'USER', true),
    ('Charlie', 'charlie', '$2a$10$kQEHTEtDudP0nblSHSZXy.IIDhZBX9P9VbCzKL8wkEXSq9bB6BYsC', 'ADMIN', true);

-- пароль для всех "fff"

-- changeset author:folk
INSERT INTO bank_card (card_num, bank_user_id, expiration_date, status, balance)
VALUES
    ('1111111111111111', 1, '2027-12-31', 'ACTIVE', 1000.00),
    ('1111111111112222', 1, '2027-12-31', 'ACTIVE', 0.00),
    ('1111111111113333', 2, '2026-06-30', 'BLOCKED', 250.50),
    ('1111111111114444', 3, '2025-03-15', 'EXPIRED', 0.00);
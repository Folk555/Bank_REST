-- changeset author:folk
-- comment: Создание тестовых пользователей
INSERT INTO bank_user (name)
VALUES
    ('Alice'),
    ('Bob'),
    ('Charlie');

-- changeset author:folk
-- Вставка банковских карт (предполагаем, что id пользователей = 1, 2, 3)
INSERT INTO bank_card (card_num, bank_user_id, expiration_date, status, balance)
VALUES
    ('4111111111111111', 1, '2027-12-31', 'ACTIVE', 1000.00),
    ('5555555555554444', 2, '2026-06-30', 'BLOCKED', 250.50),
    ('4000123412341234', 3, '2025-03-15', 'EXPIRED', 0.00);
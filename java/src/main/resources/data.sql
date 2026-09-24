-- No create endpoint exists, so the sample plans are loaded with the schema.
INSERT INTO "timeDeposits" (id, "planType", days, balance) VALUES
                                                               (1, 'basic', 31, 1000.00),
                                                               (2, 'student', 365, 1000.00),
                                                               (3, 'premium', 46, 1000.00),
                                                               (4, 'premium', 45, 1000.00);
INSERT INTO withdrawals (id, "timeDepositId", amount, date) VALUES
                                                                (1, 1, 100.00, DATE '2026-01-15'),
                                                                (2, 2, 50.00, DATE '2026-02-01');
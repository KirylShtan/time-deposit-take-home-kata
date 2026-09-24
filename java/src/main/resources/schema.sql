CREATE TABLE "timeDeposits" (
    id INTEGER PRIMARY KEY,
    "planType" VARCHAR(32) NOT NULL,
    days INTEGER NOT NULL,
    balance NUMERIC(19,2) NOT NULL
);

CREATE TABLE "withdrawals" (
    id INTEGER PRIMARY KEY,
    "timeDepositId" INTEGER NOT NULL REFERENCES "timeDeposits"(id),
    amount NUMERIC(19,2) NOT NULL,
    date DATE NOT NULL
);
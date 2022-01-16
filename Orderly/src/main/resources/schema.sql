-- SQLite
CREATE TABLE [IF NOT EXISTS] rooms (
    id INTEGER PRIMARY KEY,
    room TEXT NOT NULL UNIQUE,
    temperature REAL DEFAULT NULL
);


CREATE TABLE products (
    id INTEGER PRIMARY KEY,
    product TEXT NOT NULL UNIQUE,
    code INTEGER NOT NULL UNIQUE,
    unit TEXT NOT NULL DEFAULT 'KG',
    room_id INTEGER NOT NULL REFERENCES rooms(id),
    temperature REAL DEFAULT NULL
);

CREATE TABLE balance (
    id INTEGER PRIMARY KEY,
    room_id INTEGER NOT NULL REFERENCES rooms(id),
    product_id INTEGER NOT NULL REFERENCES products(id),
    batch INTEGER NOT NULL,
    amount REAL DEFAULT 0
);

CREATE TABLE shipments (
    id INTEGER PRIMARY KEY,
    number INTEGER NOT NULL,
    product_id INTEGER NOT NULL REFERENCES products(id),
    batch INTEGER NOT NULL,
    amount REAL DEFAULT 0
);

CREATE TABLE deliveries (
    id INTEGER PRIMARY KEY,
    number INTEGER NOT NULL,
    product_id INTEGER NOT NULL REFERENCES products(id),
    amount REAL NOT NULL
);
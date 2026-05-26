PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS giveaway_entries;
DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS auction_items;
DROP TABLE IF EXISTS authenticity_certificates;
DROP TABLE IF EXISTS giveaway_prizes;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INTEGER PRIMARY KEY,
                       name TEXT NOT NULL,
                       email TEXT NOT NULL UNIQUE,
                       role TEXT NOT NULL CHECK (role IN ('SELLER', 'BIDDER')),
                       won_auctions_count INTEGER DEFAULT 0
);

CREATE TABLE authenticity_certificates (
                                           certificate_id TEXT PRIMARY KEY,
                                           issued_by TEXT NOT NULL,
                                           issue_date TEXT NOT NULL,
                                           verified INTEGER NOT NULL CHECK (verified IN (0, 1))
);

CREATE TABLE auction_items (
                               id INTEGER PRIMARY KEY,
                               name TEXT NOT NULL,
                               sport_type TEXT NOT NULL,
                               athlete_name TEXT NOT NULL,
                               rarity_level TEXT NOT NULL,
                               item_condition TEXT NOT NULL,
                               starting_price REAL NOT NULL,
                               premium INTEGER NOT NULL CHECK (premium IN (0, 1)),
                               certificate_id TEXT,
                               FOREIGN KEY (certificate_id) REFERENCES authenticity_certificates(certificate_id)
);

CREATE TABLE auctions (
                          id INTEGER PRIMARY KEY,
                          item_id INTEGER NOT NULL,
                          seller_id INTEGER NOT NULL,
                          status TEXT NOT NULL CHECK (status IN ('OPEN', 'CLOSED')),
                          winner_id INTEGER,
                          FOREIGN KEY (item_id) REFERENCES auction_items(id),
                          FOREIGN KEY (seller_id) REFERENCES users(id),
                          FOREIGN KEY (winner_id) REFERENCES users(id)
);

CREATE TABLE bids (
                      id INTEGER PRIMARY KEY,
                      auction_id INTEGER NOT NULL,
                      bidder_id INTEGER NOT NULL,
                      amount REAL NOT NULL,
                      timestamp TEXT NOT NULL,
                      FOREIGN KEY (auction_id) REFERENCES auctions(id),
                      FOREIGN KEY (bidder_id) REFERENCES users(id)
);

CREATE TABLE giveaway_prizes (
                                 id INTEGER PRIMARY KEY,
                                 name TEXT NOT NULL,
                                 sport_type TEXT NOT NULL,
                                 description TEXT NOT NULL
);

CREATE TABLE giveaway_entries (
                                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                                  bidder_id INTEGER NOT NULL,
                                  auction_id INTEGER NOT NULL,
                                  entry_timestamp TEXT NOT NULL,
                                  FOREIGN KEY (bidder_id) REFERENCES users(id),
                                  FOREIGN KEY (auction_id) REFERENCES auctions(id)
);
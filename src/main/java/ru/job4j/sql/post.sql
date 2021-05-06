CREATE TABLE post(
                     id serial primary key,
                     name text NOT NULL,
                     info text NOT NULL,
                     link text UNIQUE,
                     created timestamp
);
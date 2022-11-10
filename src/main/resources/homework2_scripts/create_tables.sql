CREATE table IF NOT EXISTS "user"(id serial primary key,
                    name varchar(100) UNIQUE,
                    password varchar(100),
                    created_at timestamp,
                    CHECK(name != NULL AND password != NULL));
CREATE table IF NOT EXISTS post(id serial primary key,
                    text varchar(100),
                    created_at timestamp,
                    user_id integer REFERENCES "user"(id),
                    CHECK(text != NULL and user_id != NULL));
CREATE table IF NOT EXISTS comment(id serial primary key,
                    text varchar(100),
                    post_id integer REFERENCES post(id),
                    user_id integer REFERENCES "user"(id),
                    created_at timestamp,
                    CHECK (post_id != NULL and user_id != NULL));
CREATE table IF NOT EXISTS "like"(id serial primary key,
                    user_id integer NOT NULL REFERENCES "user"(id),
                    post_id integer REFERENCES post(id),
                    comment_id integer REFERENCES comment(id),
                    CHECK(comment_id != NULL or post_id != NULL));
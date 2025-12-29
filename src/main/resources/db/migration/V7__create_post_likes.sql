CREATE TABLE post_likes (
                            id UUID PRIMARY KEY,
                            post_id UUID NOT NULL,
                            user_id UUID NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE post_likes
    ADD CONSTRAINT fk_post_likes_post
        FOREIGN KEY (post_id) REFERENCES posts(id)
            ON DELETE CASCADE;

ALTER TABLE post_likes
    ADD CONSTRAINT fk_post_likes_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE;

ALTER TABLE post_likes
    ADD CONSTRAINT uk_post_user UNIQUE (post_id, user_id)
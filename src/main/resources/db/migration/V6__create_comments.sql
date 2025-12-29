CREATE TABLE comments (
                          id UUID PRIMARY KEY,
                          post_id UUID NOT NULL,
                          author_id UUID NOT NULL,
                          content TEXT NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id) REFERENCES posts(id)
            ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id) REFERENCES users(id)
            ON DELETE CASCADE;

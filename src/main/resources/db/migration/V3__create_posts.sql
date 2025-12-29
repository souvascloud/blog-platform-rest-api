CREATE TABLE posts (
                       id UUID PRIMARY KEY,
                       author_id UUID NOT NULL,
                       title VARCHAR(200) NOT NULL,
                       slug VARCHAR(220) NOT NULL,
                       content TEXT NOT NULL,
                       status VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE posts
    ADD CONSTRAINT fk_posts_author
        FOREIGN KEY (author_id) REFERENCES users(id)
            ON DELETE CASCADE;

CREATE UNIQUE INDEX idx_posts_slug ON posts(slug);
CREATE INDEX idx_posts_status ON posts(status);

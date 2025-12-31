
INSERT INTO users (
    id,
    username,
    email,
    password_hash,
    role,
    status,
    created_at
)
VALUES (
           gen_random_uuid(),
           'admin',
           'admin@blog.com',
           '$2a$10$McYV6gaFjP95LRrzdIM52OIhbcyKqxGGeUfZpWTNTMNQr/UCQfKoK',
           'ADMIN',
           'ACTIVE',
           now()
       );
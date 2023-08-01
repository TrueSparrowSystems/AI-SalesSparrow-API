CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  cookie_token VARCHAR(255) NOT NULL,
  encryption_salt VARCHAR(255) NOT NULL,
  created_at BIGINT NOT NULL,
  updated_at BIGINT NOT NULL,
  CONSTRAINT uk_email UNIQUE (email)
);


INSERT INTO users (id, email, password, cookie_token, encryption_salt, created_at, updated_at) VALUES (
    1,
    '123@gmail.com',
    'FkI/NJbSGsMXRBE9f8fJOA==',
    'XWYZaMy7c3Xg8EmAl97nww7GvzuOJHnmiRCKvxr1uSCdB1Swt0YKLZFj+v8RioCbj81s9j/BitU02UH/Awt3dFy35edDV6Lf0fgXeNBXsew=',
    'F+CMEru6FS2wl2zChHDV5XZv8veOi2KOnNZas9Vo+TkIVz6lmfyp9ya8/Q2v7KpK',
    1690488124455,
    1690488124455
);
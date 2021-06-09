CREATE TABLE auth_user(
    id bigserial NOT NULL,
    username text DEFAULT '' NOT NULL,
    email text DEFAULT '' NOT NULL,
    password text DEFAULT '' NOT NULL,
    nickname text DEFAULT '' NOT NULL,
    avatar text DEFAULT '' NOT NULL,
    is_delete smallint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX idx_auth_user_username ON auth_user(username);
CREATE UNIQUE INDEX idx_auth_user_email ON auth_user(email);
COMMENT ON COLUMN auth_user.id IS 'ID';
COMMENT ON COLUMN auth_user.username IS '用户名';
COMMENT ON COLUMN auth_user.email IS '邮箱';
COMMENT ON COLUMN auth_user.password IS '密码';
COMMENT ON COLUMN auth_user.nickname IS '昵称';
COMMENT ON COLUMN auth_user.avatar IS '头像';
COMMENT ON COLUMN auth_user.is_delete IS '是否删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN auth_user.created_at IS '创建时间';
COMMENT ON COLUMN auth_user.updated_at IS '更新时间';
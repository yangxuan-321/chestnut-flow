CREATE TABLE user_role(
    user_id bigint DEFAULT 0 NOT NULL,
    user_role smallint DEFAULT 0 NOT NULL,
    PRIMARY KEY (user_id, user_role)
);

COMMENT ON TABLE user_role IS '用户角色表';
COMMENT ON COLUMN user_role.user_id IS '用户id';
COMMENT ON COLUMN user_role.user_role IS '用户角色';
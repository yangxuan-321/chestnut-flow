CREATE TABLE chestnut_flow_parent_enum(
    id bigserial NOT NULL,
    enum_key text DEFAULT '' NOT NULL,
    enum_key_desc text DEFAULT '' NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_chestnut_flow_parent_enum_enum_key ON chestnut_flow_parent_enum(enum_key);

COMMENT ON TABLE chestnut_flow_parent_enum IS '父枚举表';
COMMENT ON COLUMN chestnut_flow_parent_enum.id IS 'ID-主键';
COMMENT ON COLUMN chestnut_flow_parent_enum.enum_key IS '键';
COMMENT ON COLUMN chestnut_flow_parent_enum.enum_key_desc IS '键描述';
COMMENT ON COLUMN chestnut_flow_parent_enum.is_deleted IS '是否被删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN chestnut_flow_parent_enum.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_parent_enum.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_parent_enum.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_parent_enum.updated_at IS '更新时间';
CREATE TABLE chestnut_flow_enum(
    id bigserial NOT NULL,
    enum_key text DEFAULT '' NOT NULL,
    enum_key_desc text DEFAULT '' NOT NULL,
    value integer DEFAULT 0 NOT NULL,
    parent_enum_id bigint DEFAULT 0 NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX idx_chestnut_flow_enum ON chestnut_flow_enum(parent_enum_id, enum_key);
COMMENT ON TABLE chestnut_flow_enum IS '枚举表';
COMMENT ON COLUMN chestnut_flow_enum.id IS 'ID-主键';
COMMENT ON COLUMN chestnut_flow_enum.enum_key IS '键';
COMMENT ON COLUMN chestnut_flow_enum.enum_key_desc IS '键描述';
COMMENT ON COLUMN chestnut_flow_enum.value IS '值';
COMMENT ON COLUMN chestnut_flow_enum.parent_enum_id IS '父枚举id';
COMMENT ON COLUMN chestnut_flow_enum.is_deleted IS '是否被删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN chestnut_flow_enum.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_enum.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_enum.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_enum.updated_at IS '更新时间';
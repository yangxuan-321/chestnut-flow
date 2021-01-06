CREATE TABLE chestnut_flow_parent_enum(
    id bigserial NOT NULL,
    enum_key text NOT NULL,
    enum_key_desc text NOT NULL,
    status smallint NOT NULL,
    created_user text NOT NULL,
    update_user text NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_parent_enum IS '父枚举表';
COMMENT ON COLUMN chestnut_flow_parent_enum.id IS 'ID-主键';
COMMENT ON COLUMN chestnut_flow_parent_enum.enum_key IS '键';
COMMENT ON COLUMN chestnut_flow_parent_enum.enum_key_desc IS '键描述';
COMMENT ON COLUMN chestnut_flow_parent_enum.status IS '枚举状态';
COMMENT ON COLUMN chestnut_flow_parent_enum.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_parent_enum.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_parent_enum.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_parent_enum.updated_at IS '更新时间';
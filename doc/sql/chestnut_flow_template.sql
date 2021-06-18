CREATE TABLE chestnut_flow_template(
    id bigserial NOT NULL,
    name text DEFAULT '' NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    create_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_chestnut_flow_template ON chestnut_flow_template(name, is_deleted);

COMMENT ON TABLE chestnut_flow_template IS '模板表';
COMMENT ON COLUMN chestnut_flow_template.id IS 'id主键';
COMMENT ON COLUMN chestnut_flow_template.name IS '模板名称';
COMMENT ON COLUMN chestnut_flow_template.is_deleted IS '是否被删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN chestnut_flow_template.create_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_template.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_template.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_template.updated_at IS '更新时间';
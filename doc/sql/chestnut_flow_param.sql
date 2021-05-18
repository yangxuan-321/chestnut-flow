CREATE TABLE chestnut_flow_param(
    id bigserial NOT NULL,
    flow_id bigint DEFAULT 0 NOT NULL,
    param_name text DEFAULT '' NOT NULL,
    param_type smallint DEFAULT 0 NOT NULL,
    param_init_value text DEFAULT '' NOT NULL,
    param_desc text DEFAULT '' NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_param IS '流程参数表';
COMMENT ON COLUMN chestnut_flow_param.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_param.flow_id IS '流程id';
COMMENT ON COLUMN chestnut_flow_param.param_name IS '参数名称';
COMMENT ON COLUMN chestnut_flow_param.param_type IS '参数类型';
COMMENT ON COLUMN chestnut_flow_param.param_init_value IS '参数初始值';
COMMENT ON COLUMN chestnut_flow_param.param_desc IS '参数备注';
COMMENT ON COLUMN chestnut_flow_param.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_param.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_param.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_param.updated_at IS '更新时间';
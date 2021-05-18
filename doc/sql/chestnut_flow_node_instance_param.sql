CREATE TABLE chestnut_flow_node_instance_param(
    id bigserial NOT NULL,
    flow_instance_id bigint DEFAULT 0 NOT NULL,
    node_instance_id bigint DEFAULT 0 NOT NULL,
    param_id bigint DEFAULT 0 NOT NULL,
    param_value text DEFAULT '' NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_node_instance_param IS '流程节点实例参数表';
COMMENT ON COLUMN chestnut_flow_node_instance_param.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_node_instance_param.flow_instance_id IS '流程实例id';
COMMENT ON COLUMN chestnut_flow_node_instance_param.node_instance_id IS '节点实例id';
COMMENT ON COLUMN chestnut_flow_node_instance_param.param_id IS '参数id';
COMMENT ON COLUMN chestnut_flow_node_instance_param.param_value IS '参数值';
COMMENT ON COLUMN chestnut_flow_node_instance_param.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_node_instance_param.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_node_instance_param.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_node_instance_param.updated_at IS '更新时间';
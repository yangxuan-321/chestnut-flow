CREATE TABLE chestnut_flow_node_instance(
    id bigserial NOT NULL,
    flow_instance_id bigint DEFAULT 0 NOT NULL,
    node_id bigint DEFAULT 0 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    param_value text DEFAULT '' NOT NULL,
    create_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_node_instance IS '流程节点实例表';
COMMENT ON COLUMN chestnut_flow_node_instance.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_node_instance.flow_instance_id IS '对应流程实例id';
COMMENT ON COLUMN chestnut_flow_node_instance.node_id IS '对应节点id';
COMMENT ON COLUMN chestnut_flow_node_instance.status IS '节点实例状态';
COMMENT ON COLUMN chestnut_flow_node_instance.param_value IS '节点实例参数json';
COMMENT ON COLUMN chestnut_flow_node_instance.create_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_node_instance.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_node_instance.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_node_instance.updated_at IS '更新时间';
CREATE TABLE chestnut_flow_node_router_instance(
    id bigserial NOT NULL,
    node_router_id bigint DEFAULT 0 NOT NULL,
    flow_instance_id bigint DEFAULT 0 NOT NULL,
    source_node_instance_id bigint DEFAULT 0 NOT NULL,
    target_node_instance_id bigint DEFAULT 0 NOT NULL,
    create_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_node_router_instance IS '流程路由实例表';
COMMENT ON COLUMN chestnut_flow_node_router_instance.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_node_router_instance.node_router_id IS '对应路由id';
COMMENT ON COLUMN chestnut_flow_node_router_instance.flow_instance_id IS '对应流程实例id';
COMMENT ON COLUMN chestnut_flow_node_router_instance.source_node_instance_id IS '源节点的节点实例id';
COMMENT ON COLUMN chestnut_flow_node_router_instance.target_node_instance_id IS '源节点的节点实例id';
COMMENT ON COLUMN chestnut_flow_node_router_instance.create_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_node_router_instance.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_node_router_instance.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_node_router_instance.updated_at IS '更新时间';
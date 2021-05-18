CREATE TABLE chestnut_flow_node_router(
    id bigserial NOT NULL,
    flow_id bigint DEFAULT 0 NOT NULL,
    node_id bigint DEFAULT 0 NOT NULL,
    next_node_id bigint DEFAULT 0 NOT NULL,
    router_rule text DEFAULT '' NOT NULL,
    description text DEFAULT '' NOT NULL,
    is_deleted smallint DEFAULT 0,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_chestnut_flow_node_router ON chestnut_flow_node_router(flow_id, node_id);

COMMENT ON TABLE chestnut_flow_node_router IS '流程路由表';
COMMENT ON COLUMN chestnut_flow_node_router.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_node_router.flow_id IS '对应的流程id';
COMMENT ON COLUMN chestnut_flow_node_router.node_id IS '开始节点(流程开始为-1 代表 开始)';
COMMENT ON COLUMN chestnut_flow_node_router.next_node_id IS '结束节点(流程结束为-1 代表 结束)';
COMMENT ON COLUMN chestnut_flow_node_router.router_rule IS '路由规则';
COMMENT ON COLUMN chestnut_flow_node_router.description IS '路由备注描述';
COMMENT ON COLUMN chestnut_flow_node_router.is_deleted IS '是否被删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN chestnut_flow_node_router.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_node_router.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_node_router.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_node_router.updated_at IS '更新时间';
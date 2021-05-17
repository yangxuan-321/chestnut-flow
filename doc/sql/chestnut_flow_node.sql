CREATE TABLE chestnut_flow_node(
    id bigserial NOT NULL,
    node_name text DEFAULT '' NOT NULL,
    node_type smallint DEFAULT 0 NOT NULL,
    icon text DEFAULT '' NOT NULL,
    node_uuid text DEFAULT '' NOT NULL,
    node_version integer DEFAULT 0 NOT NULL,
    description text DEFAULT '' NOT NULL,
    flow_id bigint DEFAULT 0 NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_chestnut_flow_node_uuid ON chestnut_flow_node(node_uuid);

COMMENT ON TABLE chestnut_flow_node IS '流程节点表';
COMMENT ON COLUMN chestnut_flow_node.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_node.node_name IS '节点名称';
COMMENT ON COLUMN chestnut_flow_node.node_type IS '节点类型';
COMMENT ON COLUMN chestnut_flow_node.icon IS '节点图标';
COMMENT ON COLUMN chestnut_flow_node.node_uuid IS 'node_uuid 就是 一个流程 纵然版本不同 uuid 也是唯一的，用来标识 是同一个模板';
COMMENT ON COLUMN chestnut_flow_node.node_version IS '节点版本';
COMMENT ON COLUMN chestnut_flow_node.description IS '节点备注描述';
COMMENT ON COLUMN chestnut_flow_node.flow_id IS '对应的流程id';
COMMENT ON COLUMN chestnut_flow_node.is_deleted IS '是否被删除 枚举 - 0 正常 1- 删除';
COMMENT ON COLUMN chestnut_flow_node.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_node.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_node.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_node.updated_at IS '更新时间';
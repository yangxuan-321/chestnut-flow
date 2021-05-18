CREATE TABLE chestnut_flow_workflow_instance(
    id bigserial NOT NULL,
    flow_id bigint DEFAULT 0 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE chestnut_flow_workflow_instance IS '流程实例表';
COMMENT ON COLUMN chestnut_flow_workflow_instance.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_workflow_instance.flow_id IS '对应的流程id';
COMMENT ON COLUMN chestnut_flow_workflow_instance.status IS '对应流程实例的状态';
COMMENT ON COLUMN chestnut_flow_workflow_instance.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_workflow_instance.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_workflow_instance.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_workflow_instance.updated_at IS '更新时间';
CREATE TABLE chestnut_flow_workflow(
    id bigserial NOT NULL,
    flow_name text DEFAULT '' NOT NULL,
    template_id bigint DEFAULT 0 NOT NULL,
    flow_version integer DEFAULT 0 NOT NULL,
    status smallint DEFAULT 0 NOT NULL,
    description text DEFAULT '' NOT NULL,
    created_user bigint DEFAULT 0 NOT NULL,
    update_user bigint DEFAULT 0 NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    updated_at timestamp DEFAULT now() NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_chestnut_flow_workflow ON chestnut_flow_workflow(template_id, flow_version);

COMMENT ON TABLE chestnut_flow_workflow IS '流程表';
COMMENT ON COLUMN chestnut_flow_workflow.id IS 'ID';
COMMENT ON COLUMN chestnut_flow_workflow.flow_name IS '流程名称';
COMMENT ON COLUMN chestnut_flow_workflow.template_id IS '对应的 模板Id 纵然版本不同 模板Id用来标识 是同一个模板';
COMMENT ON COLUMN chestnut_flow_workflow.flow_version IS '流程版本';
COMMENT ON COLUMN chestnut_flow_workflow.status IS '流程状态';
COMMENT ON COLUMN chestnut_flow_workflow.description IS '流程备注描述';
COMMENT ON COLUMN chestnut_flow_workflow.created_user IS '创建者';
COMMENT ON COLUMN chestnut_flow_workflow.update_user IS '更新者';
COMMENT ON COLUMN chestnut_flow_workflow.created_at IS '创建时间';
COMMENT ON COLUMN chestnut_flow_workflow.updated_at IS '更新时间';
CREATE TABLE chestnut_flow_workflow_json(
    template_id bigint,
    flow_version text,
    flow_data JSONb DEFAULT '{}' NOT NULL,
    PRIMARY KEY (template_id,flow_version)
);

COMMENT ON TABLE chestnut_flow_workflow_json IS '流程内容表';
COMMENT ON COLUMN chestnut_flow_workflow_json.template_id IS '流程模板id';
COMMENT ON COLUMN chestnut_flow_workflow_json.flow_version IS '对应的流程id';
COMMENT ON COLUMN chestnut_flow_workflow_json.flow_data IS '对应到前端的流程json格式';
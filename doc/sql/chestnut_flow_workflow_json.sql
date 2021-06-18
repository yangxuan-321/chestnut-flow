CREATE TABLE chestnut_flow_workflow_json(
    template_id bigint,
    flow_id bigint,
    flow_data JSONb DEFAULT '{}' NOT NULL,
    PRIMARY KEY (template_id,flow_id)
);

COMMENT ON TABLE chestnut_flow_workflow_json IS '流程实例表';
COMMENT ON COLUMN chestnut_flow_workflow_json.template_id IS '流程模板id';
COMMENT ON COLUMN chestnut_flow_workflow_json.flow_id IS '对应的流程id';
COMMENT ON COLUMN chestnut_flow_workflow_json.flow_data IS '对应到前端的流程json格式';
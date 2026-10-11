-- Generic workflow task. State values match WorkflowState.
CREATE TABLE IF NOT EXISTS `wf_task` (
  `id` varchar(64) NOT NULL,
  `state` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='workflow task';

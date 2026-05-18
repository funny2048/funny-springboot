-- 代码生成记录表
CREATE TABLE `code_gen_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_id` varchar(255) NOT NULL COMMENT '项目组ID',
  `artifact_id` varchar(255) NOT NULL COMMENT '项目ID',
  `version` varchar(50) NOT NULL COMMENT '版本号',
  `package_name` varchar(255) NOT NULL COMMENT '包名',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `components` varchar(1000) DEFAULT NULL COMMENT '组件列表（JSON格式）',
  `app_type` varchar(50) NOT NULL COMMENT '应用类型',
  `generate_time` datetime NOT NULL COMMENT '生成时间',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 正常 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成记录表';
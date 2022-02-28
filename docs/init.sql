-- 项目表
create TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '项目名称，例如: service-order',
  `member_ids` varchar(1000) NOT NULL COMMENT '项目成员id列表',
  `owner_id` bigint(20) DEFAULT '0' COMMENT '项目Owner',
  `owner_name` varchar(100) DEFAULT NULL COMMENT '所有者姓名',
  `status` tinyint(1) NOT NULL COMMENT '项目状态，0-下线，1-正常',
  `description` varchar(512) NOT NULL COMMENT '项目描述',
  PRIMARY KEY (`id`),
  KEY `project_name_IDX` (`name`) USING BTREE,
  KEY `project_owner_name_IDX` (`owner_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 告警接收的成员表
create TABLE `member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `open_id` varchar(100) NOT NULL DEFAULT '' COMMENT '三方openID',
  `union_id` varchar(100) NOT NULL DEFAULT '' COMMENT '三方UnionID',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '成员状态，0-禁用，1-启用',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`) USING BTREE,
  KEY `member_name_IDX` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 事件表，记录告警的主要信息
create TABLE if not exists `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` bigint(20) NOT NULL COMMENT '项目ID',
  `third_message_id` varchar(64) NOT NULL COMMENT '第三方消息ID',
  `chat_group_id` varchar(64) NOT NULL COMMENT '第三方群组ID',
  `detail` varchar(5000) DEFAULT NULL COMMENT '详细信息',
  `event_status` tinyint(2) NOT NULL COMMENT '事件状态，1-已创建，2-处理中，3-处理完成，99-超时关闭',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `summary` varchar(1000) DEFAULT NULL COMMENT '事件处理小结',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`event_status`) USING BTREE,
  KEY `idx_message_id` (`third_message_id`) USING BTREE,
  KEY `idx_chat_group_id` (`chat_group_id`) USING BTREE,
  KEY `idx_ct_status_project` (`create_time`,`event_status`,`project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 任务表
create TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_type` tinyint(2) NOT NULL COMMENT '任务类型',
  `data` varchar(2000) NOT NULL COMMENT '第三方消息ID',
  `status` tinyint(2) NOT NULL COMMENT '状态，0-已创建，1-处理中，2-已完成',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `trigger_time` datetime DEFAULT NULL COMMENT '触发时间',
  `result` varchar(1000) DEFAULT NULL COMMENT '任务执行结果',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`trigger_time`,`task_type`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 锁表，用于解决分布式下定时任务的问题
create TABLE `lock` (
  `key` varchar(36) NOT NULL COMMENT '主键',
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 消息表，记录告警群的每一条聊天记录
create TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_type` tinyint(2) NOT NULL COMMENT '消息类型，1-文本，2-图片，99-其他',
  `third_message_id` varchar(64) NOT NULL COMMENT '第三方消息ID',
  `chat_group_id` varchar(64) NOT NULL COMMENT '第三方群组ID',
  `content` varchar(1000) DEFAULT NULL COMMENT '消息内容',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `sender` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_third_message_id` (`third_message_id`) USING BTREE,
  KEY `idx_group_time` (`chat_group_id`,`send_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
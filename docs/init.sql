create TABLE if not exists `relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` bigint(20) NOT NULL COMMENT '源id，例如项目ID',
  `dest_id` bigint(20) NOT NULL COMMENT '目标id，例如成员ID',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联类型，例如项目和成员的关系',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_source_id_type` (`source_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

create TABLE if not exists `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(2) NOT NULL COMMENT '消息类型，1-文本，2-图片，99-其他',
  `third_message_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方消息ID',
  `chat_group_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方群组ID',
  `content` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息内容',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `sender` json NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_third_message_id` (`third_message_id`) USING BTREE,
  KEY `idx_group_time` (`chat_group_id`,`send_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*
 Navicat Premium Data Transfer

 Source Server         : fudges
 Source Server Type    : MySQL
 Source Server Version : 50734 (5.7.34)
 Source Host           : localhost:13307
 Source Schema         : lifestyle

 Target Server Type    : MySQL
 Target Server Version : 50734 (5.7.34)
 File Encoding         : 65001

 Date: 22/07/2025 16:45:12
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jot_book
-- ----------------------------
DROP TABLE IF EXISTS `jot_book`;
CREATE TABLE `jot_book`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `user_id`     bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `is_remove`   tinyint(4) NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',
    `order_num`   int(11) NULL DEFAULT NULL COMMENT '排序号',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备忘本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jot_classify
-- ----------------------------
DROP TABLE IF EXISTS `jot_classify`;
CREATE TABLE `jot_classify`
(
    `id`        bigint(20) NOT NULL AUTO_INCREMENT,
    `name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `book_id`   bigint(20) NULL DEFAULT NULL COMMENT '记录本id',
    `user_id`   bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `is_remove` tinyint(4) NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX       `idx_book_id`(`book_id`) USING BTREE,
    INDEX       `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备忘录分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jot_record
-- ----------------------------
DROP TABLE IF EXISTS `jot_record`;
CREATE TABLE `jot_record`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `title`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
    `description`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `classify_id`      bigint(20) NULL DEFAULT NULL COMMENT '分类id',
    `book_id`          bigint(20) NULL DEFAULT NULL COMMENT '备忘本id',
    `user_id`          bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `status`           int(11) NULL DEFAULT NULL COMMENT '状态，0-待处理，1-已完成，2-已忽略，3-已失败',
    `remind_type`      int(11) NULL DEFAULT 0 COMMENT '提醒类型，0-单次，1-定时任务',
    `remind_time`      datetime NULL DEFAULT NULL COMMENT '提醒时间',
    `remind_status`    int(11) NULL DEFAULT 0 COMMENT '提醒状态，0-未提醒，1-已安排，2-推送成功，3-推送失败，4-提醒中',
    `schedule_task_id` bigint(20) NULL DEFAULT NULL COMMENT '定时任务id',
    `remind_time_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '定时提醒json',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    `modify_time`      datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX              `idx_classify_id`(`classify_id`) USING BTREE,
    INDEX              `idx_user_id`(`user_id`) USING BTREE,
    INDEX              `idx_schedule_task_id`(`schedule_task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备忘记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for push_record
-- ----------------------------
DROP TABLE IF EXISTS `push_record`;
CREATE TABLE `push_record`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT,
    `url`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推送地址',
    `request`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求信息',
    `response`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回值',
    `result_code`    int(11) NULL DEFAULT NULL COMMENT '返回码',
    `target_id`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标id',
    `target_type`    int(11) NULL DEFAULT NULL COMMENT '目标类型，0:uni-push-cid',
    `target_user_id` bigint(20) NULL DEFAULT NULL COMMENT '目标人用户id',
    `source_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发送人用户id',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推送记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_record
-- ----------------------------
DROP TABLE IF EXISTS `schedule_record`;
CREATE TABLE `schedule_record`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT,
    `schedule_task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    `user_id`          bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `business_type`    int(11) NULL DEFAULT NULL COMMENT '业务类型，0-备忘录',
    `result`           int(11) NULL DEFAULT NULL COMMENT '结果，0-成功，1-失败',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX              `idx_schedule_task_id`(`schedule_task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务触发记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_task
-- ----------------------------
DROP TABLE IF EXISTS `schedule_task`;
CREATE TABLE `schedule_task`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `cron`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提醒的cron表达式',
    `trigger_times` int(11) NULL DEFAULT NULL COMMENT '触发次数',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `user_id`       bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    `modify_time`   datetime NULL DEFAULT NULL COMMENT '修改时间',
    `business_type` int(11) NULL DEFAULT NULL COMMENT '业务类型，0-备忘录',
    `status`        int(11) NULL DEFAULT 0 COMMENT '状态，0-开启，1-关闭',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_base
-- ----------------------------
DROP TABLE IF EXISTS `user_base`;
CREATE TABLE `user_base`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `mobile_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
    `uni_push_cid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'uni-push2的client id',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `modify_time`  datetime NULL DEFAULT NULL COMMENT '修改时间',
    `is_remove`    int(11) NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

INSERT INTO `user_base` (`id`, `name`, `mobile_phone`, `uni_push_cid`, `create_time`, `modify_time`, `is_remove`)
VALUES (null, 'test', '1', '', now(), now(), 0);

-- ----------------------------
-- Table structure for user_password
-- ----------------------------
DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码，加密',
    `salt`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
    `length`   int(11) NULL DEFAULT NULL COMMENT '密码位数',
    `user_id`  bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX      `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户密码表' ROW_FORMAT = Dynamic;

INSERT INTO `user_password` (`id`, `password`, `salt`, `length`, `user_id`)
VALUES (null, '77d3b7ed9db7d236b9eac8262d27f6a5', '123', 6, 1);

SET
FOREIGN_KEY_CHECKS = 1;


SET
FOREIGN_KEY_CHECKS=0;

CREATE TABLE `lifestyle_prod`.`file_upload`
(
    `id`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `file_path`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
    `name`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `suffix`        varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '格式，后缀',
    `size` double NULL DEFAULT NULL COMMENT '大小，单位kb',
    `business_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名称',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `user_id`       bigint(20) NULL DEFAULT NULL COMMENT '用户id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_create_time`(`create_time`) USING BTREE,
    INDEX           `idx_suffix`(`suffix`) USING BTREE,
    INDEX           `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `lifestyle_prod`.`user_base`
    ADD COLUMN `img_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像图片地址' AFTER `name`;

ALTER TABLE `lifestyle_prod`.`user_base`
    ADD COLUMN `birthday` datetime NULL DEFAULT NULL COMMENT '生日' AFTER `img_url`;

ALTER TABLE `lifestyle_prod`.`user_base`
    ADD COLUMN `sex` tinyint(4) NULL DEFAULT 0 COMMENT '性别，0-未知，1-男，2-女' AFTER `birthday`;

ALTER TABLE `lifestyle_prod`.`user_base`
    ADD COLUMN `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名' AFTER `sex`;

ALTER TABLE `lifestyle_prod`.`user_base`
    ADD UNIQUE INDEX `idx_mobile_phoe`(`mobile_phone`) USING BTREE;

SET
FOREIGN_KEY_CHECKS=1;
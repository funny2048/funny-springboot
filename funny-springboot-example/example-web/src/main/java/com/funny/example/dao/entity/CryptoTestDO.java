package com.funny.example.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.funny.framework.crypto.handler.EncryptTypeHandler;
import com.funny.framework.crypto.handler.HashTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 加解密测试表
 *
 * DDL:
 * CREATE TABLE crypto_test (
 *     id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
 *     name varchar(100) NOT NULL COMMENT '名称',
 *     phone_encrypt varchar(500) DEFAULT NULL COMMENT '加密手机号',
 *     phone_hash varchar(64) DEFAULT NULL COMMENT '手机号hash',
 *     is_del tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0 正常 1 删除',
 *     created_stime datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 *     modified_stime datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 *     PRIMARY KEY (id)
 * ) COMMENT='加解密测试表';
 */
@Getter
@Setter
@TableName(value = "crypto_test", autoResultMap = true)
public class CryptoTestDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    /**
     * 加密手机号 - 写入时自动加密，读取时自动解密
     */
    @TableField(value = "phone_encrypt", typeHandler = EncryptTypeHandler.class)
    private String phoneEncrypt;

    /**
     * 手机号hash - 写入时自动取MD5，读取时原样返回（不可逆）
     */
    @TableField(value = "phone_hash", typeHandler = HashTypeHandler.class)
    private String phoneHash;

    @TableField("is_del")
    @TableLogic
    private Integer isDel;

    @TableField("created_stime")
    private LocalDateTime createdStime;

    @TableField("modified_stime")
    private LocalDateTime modifiedStime;
}

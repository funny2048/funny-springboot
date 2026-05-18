package com.funny.framework.codegen.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 代码生成记录表
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:27:30
 */
@Getter
@Setter
@TableName("code_gen_record")
public class CodeGenRecordDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目组ID
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 项目ID
     */
    @TableField("artifact_id")
    private String artifactId;

    /**
     * 版本号
     */
    @TableField("version")
    private String version;

    /**
     * 包名
     */
    @TableField("package_name")
    private String packageName;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 组件列表（JSON格式）
     */
    @TableField("components")
    private String components;

    /**
     * 应用类型
     */
    @TableField("app_type")
    private String appType;

    /**
     * 生成时间
     */
    @TableField("generate_time")
    private LocalDateTime generateTime;

    /**
     * 是否删除 0 正常 1 删除
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;
}
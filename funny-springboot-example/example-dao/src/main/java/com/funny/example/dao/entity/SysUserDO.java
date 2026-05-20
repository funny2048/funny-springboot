package com.funny.example.dao.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author fangli
 * @since 2024-12-09 07:14:55
 */
@Getter
@Setter
@TableName("sys_user")
public class SysUserDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户编码
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 登录账号
     */
    @TableField("user_name")
    private String userName;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 工号，唯一键
     */
    @TableField("work_no")
    private String workNo;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * md5密码盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 电子邮件
     */
    @TableField("email")
    private String email;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 性别(1-正常,2-冻结)
     */
    @TableField("status")
    private Boolean status;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 更新时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    /**
     * 删除状态(0-正常,1-已删除)
     */
    @TableField("is_del")
    @TableLogic
    private Boolean isDel;
}

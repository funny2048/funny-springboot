package com.funny.framework.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.funny.framework.codegen.dao.entity.CodeGenRecordDO;
import com.funny.framework.codegen.domain.CodegenParam;

import java.util.Map;

/**
 * <p>
 * 代码生成记录表 服务类
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:27:30
 */
public interface ICodeGenRecordService extends IService<CodeGenRecordDO> {
    void saveCodeGenRecord(CodegenParam codegenParam,boolean success);
}
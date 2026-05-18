package com.funny.framework.codegen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.funny.framework.codegen.dao.entity.CodeGenRecordDO;
import com.funny.framework.codegen.dao.mapper.CodeGenRecordMapper;
import com.funny.framework.codegen.domain.CodegenParam;
import com.funny.framework.codegen.service.ICodeGenRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 代码生成记录表 服务实现类
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:27:30
 */
@Service
public class CodeGenRecordServiceImpl extends ServiceImpl<CodeGenRecordMapper, CodeGenRecordDO> implements ICodeGenRecordService {
    @Resource
    private CodeGenRecordMapper codeGenRecordMapper;

    @Override
    public void saveCodeGenRecord(CodegenParam codegenParam,boolean success) {
        if(!success){
            return;
        }
        CodeGenRecordDO codeGenRecordDO = new CodeGenRecordDO();
        codeGenRecordDO.setAppType(codegenParam.getAppType());
        codeGenRecordDO.setComponents(String.join(",", codegenParam.getComponents()));
        codeGenRecordDO.setDescription(codegenParam.getDescription());
        codeGenRecordDO.setGroupId(codegenParam.getGroupId());
        codeGenRecordDO.setArtifactId(codegenParam.getArtifactId());
        codeGenRecordDO.setVersion(codegenParam.getVersion());
        codeGenRecordDO.setPackageName(codegenParam.getPackageName());
        codeGenRecordDO.setGenerateTime(LocalDateTime.now());
        codeGenRecordMapper.insert(codeGenRecordDO);
    }
}
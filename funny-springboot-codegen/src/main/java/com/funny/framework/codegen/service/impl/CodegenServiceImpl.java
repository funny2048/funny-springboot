package com.funny.framework.codegen.service.impl;

import com.funny.framework.codegen.domain.CodegenParam;
import com.funny.framework.codegen.service.CodegenService;
import com.funny.framework.codegen.service.ICodeGenRecordService;
import com.funny.framework.codegen.utils.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CodegenServiceImpl implements CodegenService {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ICodeGenRecordService codeGenRecordService;

    @Override
    public String generateCode(CodegenParam codegenParam) {
        try {
            Map<String, String> dataModel = BeanUtils.describe(codegenParam);
            dataModel.put("package", codegenParam.getPackageName());

            // 示例数据模型
            dataModel.put("system", "arch");
            dataModel.put("project", "auto-app");
            dataModel.put("jobRegisterProd", "http://framework-job.funny.com");
            dataModel.put("jobRegisterTest", "http://framework-job.funny.com");
            // 加载classpath根目录资源（"classpath:"为Spring资源前缀）
            Resource resource = resourceLoader.getResource("classpath:");
            File file = resource.getFile();
            String templateDir = file.getAbsolutePath() + "/templates";
            templateDir += File.separator + codegenParam.getSpringbootVersion();
            templateDir += File.separator + codegenParam.getAppType();
            String outputDir = "/data/generated-files/" + UUID.randomUUID();
            String result = FreemarkerUtil.generateFilesFromTemplates(templateDir, outputDir, dataModel);
            codeGenRecordService.saveCodeGenRecord(codegenParam, true);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("generateCode error, codegenParam: {}", codegenParam, e);
            codeGenRecordService.saveCodeGenRecord(codegenParam, false);
        }
        return null;
    }
}

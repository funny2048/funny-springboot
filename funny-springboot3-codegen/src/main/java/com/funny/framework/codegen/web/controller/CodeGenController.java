package com.funny.framework.codegen.web.controller;

import com.funny.framework.codegen.domain.CodegenParam;
import com.funny.framework.codegen.service.CodegenService;
import com.funny.framework.tool.IPUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;


/**
 *
 **/
@RestController
public class CodeGenController {
    @Resource
    private CodegenService codegenService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> downloadZip(HttpServletRequest request, @RequestBody CodegenParam codegenParam) throws IOException {
        // 创建内存中的输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String zipFilePath = codegenService.generateCode(codegenParam);
        // 使用 NIO 读取文件内容并写入 byteArrayOutputStream
        byte[] fileBytes = Files.readAllBytes(Paths.get(zipFilePath));
        byteArrayOutputStream.write(fileBytes);
        String zipFileName  = zipFilePath.substring(zipFilePath.lastIndexOf(File.separator) + 1);

        // 设置HTTP响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", zipFileName);

        // 返回ZIP文件
        return new ResponseEntity<>(
                byteArrayOutputStream.toByteArray(),
                headers,
                HttpStatus.OK
        );
    }
}

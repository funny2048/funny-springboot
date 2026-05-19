package com.funny.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.funny.example.client.CodegenFeignClient;
import com.funny.framework.core.result.ApiResult;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test/feign")
@RequiredArgsConstructor
public class FeignTestController {

    @Resource
    private CodegenFeignClient codegenFeignClient;

    @GetMapping("/heartbeat")
    public ApiResult<String> heartbeat() {
        log.info("feign call codegen heartbeat");
        String result = codegenFeignClient.heartbeat();
        return ApiResult.succ(result);
    }
}

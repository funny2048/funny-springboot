package com.funny.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.funny.example.dao.entity.CryptoTestDO;
import com.funny.example.dao.mapper.CryptoTestMapper;
import com.funny.framework.core.result.ApiResult;
import com.funny.framework.crypto.model.CryptoEntry;
import com.funny.framework.crypto.utils.CryptoUtils;
import com.funny.framework.log.context.ContextAwareExecutor;
import com.funny.framework.log.context.ContextAwareSpringExecutor;
import com.funny.framework.log.tracing.TraceAwareTask;
import com.funny.framework.redis.DistributedLock;
import com.funny.framework.redis.RedisLock;
import com.funny.framework.redis.RedisLockManager;
import com.funny.framework.sign.annotation.AppIdCheck;
import com.funny.framework.sign.annotation.SignVerify;
import com.funny.framework.sign.helper.SignHelper;
import com.funny.framework.sign.model.SignParamMap;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能测试控制器 - 测试 sign 签名 / redis 分布式锁 / crypto 加解密
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class FeatureTestController {

    @Autowired
    private CryptoUtils cryptoUtils;

    @Autowired
    private RedisLockManager redisLockManager;

    @Autowired
    private CryptoTestMapper cryptoTestMapper;

    // ==================== Sign 模块测试 ====================

    /**
     * 生成签名 - 手动调用 SignHelper 演示签名算法
     * 算法: MD5(appKey + 按key排序的参数拼接 + appKey).toUpperCase()
     *
     * @param appKey  应用密钥
     * @param bizParam 业务参数（可选）
     */
    @PostMapping("/sign/generate")
    public ApiResult<Map<String, String>> generateSign(
            @RequestParam("appKey") String appKey,
            @RequestParam(value = "bizParam", required = false) String bizParam) {
        log.info("generateSign, appKey={}, bizParam={}", appKey, bizParam);

        SignParamMap params = new SignParamMap();
        if (bizParam != null) {
            params.put("bizParam", bizParam);
        }
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        params.put(SignHelper.TIMESTAMP_FIELD, timestamp);

        String sign = SignHelper.getSign(appKey, params);

        Map<String, String> result = new HashMap<>();
        result.put("sign", sign);
        result.put("_timestamp", timestamp);
        result.put("_sign", sign);
        result.put("algorithm", "MD5(appKey + sortedParams[key+value] + appKey).toUpperCase()");
        return ApiResult.succ(result);
    }

    /**
     * 签名验证接口 - 需要 sign 基础设施（AppIdService + SignCache）
     * 请求时需携带: appId, _timestamp, _sign 参数
     * 签名计算方式同 /test/sign/generate，appKey 从 appId 对应的配置中获取
     */
    @SignVerify
    @PostMapping("/sign/protected")
    public ApiResult<String> signProtected() {
        return ApiResult.succ("签名验证通过");
    }

    /**
     * AppId 检查接口 - 仅验证 appId 是否合法（不校验签名）
     * 请求时需携带: appId 参数
     */
    @AppIdCheck
    @GetMapping("/sign/appid-check")
    public ApiResult<String> appIdCheck() {
        return ApiResult.succ("appId验证通过");
    }

    // ==================== Redis 分布式锁测试 ====================

    /**
     * 手动获取/释放分布式锁 - 使用 RedisLockManager
     * 演示 try-with-resources 模式自动释放
     *
     * @param key      锁的业务key
     * @param expireMs 锁过期时间（毫秒）
     */
    @GetMapping("/lock/manual")
    public ApiResult<Map<String, Object>> manualLock(
            @RequestParam("key") String key,
            @RequestParam(value = "expireMs", defaultValue = "10000") long expireMs) {
        log.info("manualLock, key={}, expireMs={}", key, expireMs);

        String lockKey = "test:lock:" + key;
        RedisLock lock = null;
        try {
            lock = redisLockManager.fetchAndTryLock(lockKey, expireMs);
            Map<String, Object> result = new HashMap<>();
            result.put("lockKey", lockKey);
            result.put("acquired", true);
            result.put("lockValue", lock.getValue());
            return ApiResult.succ(result);
        } catch (Exception e) {
            log.error("获取分布式锁失败, lockKey={}", lockKey, e);
            return ApiResult.fail("获取锁失败: " + e.getMessage());
        } finally {
            if (lock != null) {
                lock.releaseLock();
            }
        }
    }

    /**
     * 注解式分布式锁 - 使用 @DistributedLock 注解
     * AOP 自动加锁/释放，业务代码无感知
     *
     * @param taskId 任务ID，会作为锁的一部分
     */
    @DistributedLock(key = "'test:lock:annotation:' + #taskId", expireInMilliseconds = 10000)
    @GetMapping("/lock/annotation")
    public ApiResult<String> annotationLock(@RequestParam("taskId") String taskId) {
        log.info("annotationLock, taskId={}", taskId);
        return ApiResult.succ("注解式分布式锁获取成功, taskId=" + taskId);
    }

    /**
     * 并发竞争锁测试 - 模拟多线程竞争同一把锁
     * 验证分布式锁的互斥性：同一时刻只有一个线程能获取到锁
     *
     * @param threadCount 并发线程数
     */
    @GetMapping("/lock/concurrent")
    public ApiResult<Map<String, Object>> concurrentLock(
            @RequestParam(value = "threadCount", defaultValue = "5") int threadCount) {
        log.info("concurrentLock, threadCount={}", threadCount);

        String lockKey = "test:lock:concurrent:" + System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                RedisLock lock = null;
                try {
                    lock = redisLockManager.fetch(lockKey, 5000);
                    if (lock.tryLock()) {
                        successCount.incrementAndGet();
                        log.info("线程{}获取锁成功", index);
                        Thread.sleep(1000);
                    } else {
                        failCount.incrementAndGet();
                        log.info("线程{}获取锁失败", index);
                    }
                } catch (Exception e) {
                    log.error("线程{}执行异常", index, e);
                } finally {
                    if (lock != null) {
                        lock.releaseLock();
                    }
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        Map<String, Object> result = new HashMap<>();
        result.put("lockKey", lockKey);
        result.put("threadCount", threadCount);
        result.put("successCount", successCount.get());
        result.put("failCount", failCount.get());
        return ApiResult.succ(result);
    }

    // ==================== Crypto 加解密测试 ====================

    /**
     * 加密文本 - 使用 CryptoUtils (AES/GCM 模式)
     * 返回: 原文、密文、MD5 hash
     */
    @GetMapping("/crypto/encrypt")
    public ApiResult<CryptoEntry> encrypt(@RequestParam("text") String text) {
        log.info("encrypt, text={}", text);
        try {
            CryptoEntry entry = cryptoUtils.encrypt(text);
            return ApiResult.succ(entry);
        } catch (Exception e) {
            log.error("加密失败, text={}", text, e);
            return ApiResult.fail("加密失败: " + e.getMessage());
        }
    }

    /**
     * 解密文本 - 使用 CryptoUtils
     * 传入密文，返回原文和 hash
     */
    @GetMapping("/crypto/decrypt")
    public ApiResult<CryptoEntry> decrypt(@RequestParam("encryptedText") String encryptedText) {
        log.info("decrypt, encryptedText={}", encryptedText);
        try {
            CryptoEntry entry = cryptoUtils.decrypt(encryptedText);
            return ApiResult.succ(entry);
        } catch (Exception e) {
            log.error("解密失败, encryptedText={}", encryptedText, e);
            return ApiResult.fail("解密失败: " + e.getMessage());
        }
    }

    /**
     * 完整加解密流程 - 加密后再解密，验证数据一致性
     */
    @GetMapping("/crypto/round-trip")
    public ApiResult<Map<String, String>> roundTrip(@RequestParam("text") String text) {
        log.info("roundTrip, text={}", text);

        try {
            CryptoEntry encrypted = cryptoUtils.encrypt(text);
            CryptoEntry decrypted = cryptoUtils.decrypt(encrypted.getEncrypted());

        Map<String, String> result = new HashMap<>();
        result.put("original", text);
        result.put("encrypted", encrypted.getEncrypted());
        result.put("decrypted", decrypted.getOrigin());
        result.put("hash", encrypted.getHash());
        result.put("match", String.valueOf(text.equals(decrypted.getOrigin())));
            return ApiResult.succ(result);
        } catch (Exception e) {
            log.error("加解密流程失败, text={}", text, e);
            return ApiResult.fail("加解密失败: " + e.getMessage());
        }
    }

    /**
     * 数据库加密写入 - 验证 EncryptTypeHandler / HashTypeHandler
     * phone_encrypt 字段写入时自动加密，读取时自动解密
     * phone_hash 字段写入时自动取 MD5 hash（不可逆）
     *
     * 需要先创建 crypto_test 表，DDL 见 CryptoTestDO 类注释
     */
    @GetMapping("/crypto/db-save")
    public ApiResult<Long> saveCryptoRecord(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone) {
        log.info("saveCryptoRecord, name={}, phone={}", name, phone);

        CryptoTestDO entity = new CryptoTestDO();
        entity.setName(name);
        // EncryptTypeHandler 自动加密，HashTypeHandler 自动取 MD5
        entity.setPhoneEncrypt(phone);
        entity.setPhoneHash(phone);
        cryptoTestMapper.insert(entity);
        return ApiResult.succ(entity.getId());
    }

    /**
     * 数据库加密读取 - 验证读取时自动解密
     * phone_encrypt 读取后是明文，phone_hash 读取后是 MD5 值
     */
    @GetMapping("/crypto/db-get")
    public ApiResult<CryptoTestDO> getCryptoRecord(@RequestParam("id") Long id) {
        log.info("getCryptoRecord, id={}", id);
        CryptoTestDO entity = cryptoTestMapper.selectById(id);

        return ApiResult.succ(entity);
    }


    @GetMapping("/thread/trace")
    public ApiResult threadTrace() {
        log.info("threadTrace, threadId={}", Thread.currentThread().threadId());
        ContextAwareExecutor executor = new ContextAwareExecutor(4, 8, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        executor.submit(() -> {
            log.info("executor.submit threadId={}", Thread.currentThread().threadId());
        });

        // Spring 线程池
        ContextAwareSpringExecutor springExecutor = new ContextAwareSpringExecutor();
        springExecutor.setCorePoolSize(4);
        springExecutor.setMaxPoolSize(8);
        springExecutor.setQueueCapacity(100);
        springExecutor.setThreadNamePrefix("ctx-aware-");
        springExecutor.initialize();
        springExecutor.submit(() -> {
            log.info("springExecutor.submit threadId={}", Thread.currentThread().threadId());
        });

        // 单任务追踪
        new TraceAwareTask() {
            @Override
            protected void execute() {
                log.info("TraceAwareTask threadId={}", Thread.currentThread().threadId());
            }
        }.start();
        return ApiResult.succ();
    }
}




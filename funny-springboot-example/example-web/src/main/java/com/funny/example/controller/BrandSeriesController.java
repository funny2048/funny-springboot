package com.funny.example.controller;

import com.funny.framework.core.result.ApiResult;
import com.funny.framework.redis.RedisClient;
import com.funny.example.api.SeriesApi;
import com.funny.example.dao.entity.BrandSeriesDO;
import com.funny.example.model.request.SeriesRequest;
import com.funny.example.model.response.SeriesResponse;
import com.funny.example.service.IBrandSeriesService;
import com.funny.framework.tool.BeanCopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 品牌车系表 前端控制器
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:28:35
 */
@RestController
public class BrandSeriesController implements SeriesApi {
    @Autowired
    private IBrandSeriesService brandSeriesService;

    @Autowired
    private RedisClient redisClientWrapper;

    @Override
    public ApiResult<SeriesResponse> getSeriesById(SeriesRequest seriesRequest) {
        BrandSeriesDO brandSeriesDO = brandSeriesService.getById(seriesRequest.getSeriesId());
        SeriesResponse seriesResponse = new SeriesResponse();
        BeanCopyHelper.copy(brandSeriesDO, seriesResponse);
        return ApiResult.succ(seriesResponse);
    }

    @RequestMapping("/series/all")
    public ApiResult<List<BrandSeriesDO>> getAllSeries() {
        List<BrandSeriesDO> brandSeriesDOList = brandSeriesService.list();
        return ApiResult.succ(brandSeriesDOList);
    }

    @RequestMapping("/cache/set")
    public ApiResult setCache(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisClientWrapper.set(key, value);
        return ApiResult.succ(key + "=" + value);
    }

    @RequestMapping("/cache/get")
    public ApiResult getCache(@RequestParam("key") String key) {
        String test = redisClientWrapper.get(key);
        return ApiResult.succ(test);
    }
}

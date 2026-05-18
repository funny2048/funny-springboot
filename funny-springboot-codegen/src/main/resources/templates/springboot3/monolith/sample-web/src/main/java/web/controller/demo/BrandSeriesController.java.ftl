
package ${package}.web.controller.demo;

import com.funny.framework.core.result.ApiResult;
import com.funny.framework.redis.IRedisClientWrapper;
import com.funny.framework.tool.BeanCopyHelper;
import ${package}.api.SeriesApi;
import ${package}.dao.entity.BrandSeriesDO;
import ${package}.model.request.SeriesRequest;
import ${package}.model.response.SeriesResponse;
import ${package}.service.IBrandSeriesService;

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
    private IRedisClientWrapper redisClientWrapper;

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

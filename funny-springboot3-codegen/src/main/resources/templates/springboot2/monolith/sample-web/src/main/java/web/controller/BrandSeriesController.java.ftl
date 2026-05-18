
package ${package}.web.controller;

import com.alibaba.fastjson.JSON;
import com.funny.framework.core.result.ApiResult;
import com.funny.framework.tool.BeanCopyHelper;
import ${package}.api.SeriesApi;
import ${package}.dao.entity.BrandSeriesDO;
import ${package}.model.request.SeriesRequest;
import ${package}.model.response.SeriesResponse;
import ${package}.service.IBrandSeriesService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @Resource
    private IBrandSeriesService brandSeriesService;

    @Override
    public ApiResult<SeriesResponse> getSeriesById(SeriesRequest seriesRequest) {
        BrandSeriesDO brandSeriesDO = brandSeriesService.getById(seriesRequest.getSeriesId());
        SeriesResponse seriesResponse = new SeriesResponse();
        System.out.println(JSON.toJSONString(seriesRequest));
        BeanCopyHelper.copy(brandSeriesDO, seriesResponse);
        return ApiResult.succ(seriesResponse);
    }
}

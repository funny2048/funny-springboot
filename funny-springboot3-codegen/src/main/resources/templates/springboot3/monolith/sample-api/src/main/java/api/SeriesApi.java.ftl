
package ${package}.api;

import com.funny.framework.core.result.ApiResult;
import ${package}.model.request.SeriesRequest;
import ${package}.model.response.SeriesResponse;
import org.springframework.web.bind.annotation.GetMapping;

/**
 */
public interface SeriesApi {
    @GetMapping("/series/id")
    ApiResult<SeriesResponse> getSeriesById(SeriesRequest seriesRequest);
}

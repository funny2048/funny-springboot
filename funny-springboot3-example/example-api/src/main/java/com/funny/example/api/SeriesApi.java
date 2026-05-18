package com.funny.example.api;

import com.funny.framework.core.result.ApiResult;
import com.funny.example.model.request.SeriesRequest;
import com.funny.example.model.response.SeriesResponse;
import org.springframework.web.bind.annotation.GetMapping;

/**
 */
public interface SeriesApi {
    @GetMapping("/series/id")
    ApiResult<SeriesResponse> getSeriesById(SeriesRequest seriesRequest);
}

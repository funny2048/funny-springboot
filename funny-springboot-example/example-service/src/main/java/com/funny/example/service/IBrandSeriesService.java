package com.funny.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.funny.example.dao.entity.BrandSeriesDO;

/**
 * <p>
 * 品牌车系表 服务类
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:28:35
 */
public interface IBrandSeriesService extends IService<BrandSeriesDO> {

    void queryBrandSeries(BrandSeriesDO brandSeriesDO);
}

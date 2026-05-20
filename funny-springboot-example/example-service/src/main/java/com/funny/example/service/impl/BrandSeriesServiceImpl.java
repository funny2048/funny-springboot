package com.funny.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.funny.example.dao.entity.BrandSeriesDO;
import com.funny.example.dao.mapper.BrandSeriesMapper;
import com.funny.example.service.IBrandSeriesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌车系表 服务实现类
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:28:35
 */
@Service
public class BrandSeriesServiceImpl extends ServiceImpl<BrandSeriesMapper, BrandSeriesDO> implements IBrandSeriesService {

}

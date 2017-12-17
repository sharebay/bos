package com.fly.bos.service;

import com.fly.bos.domain.base.TransitInfo;

import java.util.List;

public interface TransitInfoService extends BaseService<TransitInfo, Integer> {
    void beginTransit(List<Integer> ids);
}

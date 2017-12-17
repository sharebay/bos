package com.fly.bos.service;

import com.fly.bos.domain.base.FixedArea;

public interface FixedAreaService extends BaseService<FixedArea, String> {

    void associateCourier(String id, Integer courierId, Integer takeTimeId);
}

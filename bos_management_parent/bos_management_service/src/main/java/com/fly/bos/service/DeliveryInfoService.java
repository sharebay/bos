package com.fly.bos.service;

import com.fly.bos.domain.base.DeliveryInfo;

public interface DeliveryInfoService extends BaseService<DeliveryInfo, Integer> {
    /**
     * 保存DeliveryInfo并关联transitinfo
     * @param model
     * @param transitInfoId
     */
    void save(DeliveryInfo model, Integer transitInfoId);
}

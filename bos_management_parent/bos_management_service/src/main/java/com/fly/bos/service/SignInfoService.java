package com.fly.bos.service;

import com.fly.bos.domain.base.SignInfo;

public interface SignInfoService extends BaseService<SignInfo, Integer> {
    /**
     * 保存对象，关联transitInfo
     * @param model
     * @param transitInfoId
     */
    void save(SignInfo model, Integer transitInfoId);
}

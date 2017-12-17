package com.fly.bos.service;

import com.fly.bos.domain.base.InOutStorageInfo;

public interface InOutStorageInfoService extends BaseService<InOutStorageInfo, Integer> {
    /**
     * 保存InOutStorageInfo并关联transitInfoId
     * @param inOutStorageInfo
     * @param transitInfoId
     */
    void save(InOutStorageInfo inOutStorageInfo, Integer transitInfoId);
}

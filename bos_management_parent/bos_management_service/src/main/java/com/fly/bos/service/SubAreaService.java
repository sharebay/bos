package com.fly.bos.service;

import com.fly.bos.domain.base.FixedArea;
import com.fly.bos.domain.base.SubArea;

import java.util.Set;

public interface SubAreaService extends BaseService<SubArea, String> {

    /**
     * 查找未关联定区的分区
     * @return
     */
    Set<SubArea> findByFixedAreaIsNull();

    /**
     * 查找关联指定定区的所有分区
     * @param fixedArea
     * @return
     */
    Set<SubArea> findByFixedArea(FixedArea fixedArea);
}

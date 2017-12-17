package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.FixedArea;
import com.fly.bos.domain.base.SubArea;

import java.util.Set;

public interface SubAreaDao extends BaseDao<SubArea, String> {

    /**
     * 查找未关联定区的分区
     * @return
     */
    Set<SubArea> findByFixedAreaIsNull();

    /**
     * 查找关联指定丁区的所有分区
     * @param fixedArea
     * @return
     */
    Set<SubArea> findByFixedArea(FixedArea fixedArea);
}

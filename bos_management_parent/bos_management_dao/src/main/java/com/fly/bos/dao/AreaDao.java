package com.fly.bos.dao;

import com.fly.bos.base.dao.BaseDao;
import com.fly.bos.domain.base.Area;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaDao extends BaseDao<Area, String> {

    /**
     *  通过简码搜索地区
     * @param shortcode
     * @return
     */
    @Query("from Area where shortcode = ?1")
    List<Area> findByShortcode(String shortcode);

    /**
     * 通过省市区获取地区
     * @param province
     * @param city
     * @param district
     * @return
     */
    Area findByProvinceAndCityAndDistrict(String province, String city, String district);
}

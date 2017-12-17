package com.fly.bos.service;

import com.fly.bos.domain.base.Area;

import java.util.List;

public interface AreaService extends BaseService<Area, String> {

    /**
     * 通过简码搜索地区
     * @param shortcode
     * @return
     */
    List<Area> findByShortcode(String shortcode);
}

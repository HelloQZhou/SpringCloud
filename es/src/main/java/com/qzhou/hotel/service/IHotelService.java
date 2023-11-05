package com.qzhou.hotel.service;

import com.qzhou.hotel.pojo.Hotel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qzhou.hotel.pojo.RequestParams;
import com.qzhou.hotel.pojo.PageResult;

public interface IHotelService extends IService<Hotel> {

    //根据搜索关键字分页查询
    PageResult search(RequestParams requestParams);
}

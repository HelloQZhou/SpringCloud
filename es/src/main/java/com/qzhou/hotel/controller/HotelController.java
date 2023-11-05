package com.qzhou.hotel.controller;

import com.qzhou.hotel.pojo.RequestParams;
import com.qzhou.hotel.pojo.PageResult;
import com.qzhou.hotel.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private IHotelService hotelService;

    @PostMapping("/list")
    public PageResult search(@RequestBody RequestParams requestParams){
        return hotelService.search(requestParams);
    }

}

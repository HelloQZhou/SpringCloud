package com.qzhou.hotel.service.impl;

import com.qzhou.hotel.mapper.HotelMapper;
import com.qzhou.hotel.pojo.Hotel;
import com.qzhou.hotel.service.IHotelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
}

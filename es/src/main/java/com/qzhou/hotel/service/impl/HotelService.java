package com.qzhou.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qzhou.hotel.mapper.HotelMapper;
import com.qzhou.hotel.pojo.Hotel;
import com.qzhou.hotel.pojo.HotelDoc;
import com.qzhou.hotel.pojo.RequestParams;
import com.qzhou.hotel.pojo.PageResult;
import com.qzhou.hotel.service.IHotelService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

    @Autowired
    private RestHighLevelClient client;


    @Override
    //根据搜索关键字分页查询
    public PageResult search(RequestParams requestParams) {
        try {
            String key = requestParams.getKey();
            int size = requestParams.getSize();
            int page = requestParams.getPage();

            SearchRequest request=new SearchRequest("hotel");

            if (!StringUtils.isEmpty(key)) {
                //request.source().query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("all",key)));
               request.source().query(QueryBuilders.matchQuery("all",key));
            }
            else {
               // request.source().query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()));
                request.source().query(QueryBuilders.matchAllQuery());
            }

            request.source().from((page - 1) * size).size(size);

            //执行DSL语句后 响应的数据
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析响应
            return  handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("搜索数据失败", e);
        }


    }

    private PageResult handleResponse(SearchResponse response) {
        SearchHits Searchhits = response.getHits();

        long total = Searchhits.getTotalHits().value;

        SearchHit[] hits = Searchhits.getHits();

        List<HotelDoc> hotels = new ArrayList<>(hits.length);
        for (SearchHit hit : hits) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);

            hotels.add(hotelDoc);
        }

        return new PageResult(total,hotels);
    }




}

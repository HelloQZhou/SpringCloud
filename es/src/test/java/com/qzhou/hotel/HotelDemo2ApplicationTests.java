package com.qzhou.hotel;

import com.alibaba.fastjson.JSON;
import com.qzhou.hotel.pojo.Hotel;
import com.qzhou.hotel.pojo.HotelDoc;
import com.qzhou.hotel.service.impl.HotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static com.qzhou.hotel.constants.HotelConstants.MAPPING_TEMPLATE;


@SpringBootTest
class HotelDemo2ApplicationTests {

    @Autowired
    private HotelService service;

    private RestHighLevelClient client;


    @Test
    //新增 文档
    void testCreateDoc() throws IOException {
        Hotel hotel = service.getById(60214L);

        HotelDoc hotelDoc = new HotelDoc(hotel);

        IndexRequest request=new IndexRequest("hotel").id(hotel.getId().toString());

        request.source(JSON.toJSONString(hotelDoc),XContentType.JSON);

        IndexResponse index = client.index(request, RequestOptions.DEFAULT);

        System.out.println("index="+index);

    }
    @Test
    //获取 文档
    void testGetDoc() throws IOException {
        GetRequest request=new GetRequest("hotel","60214");

        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        String sourceAsString = response.getSourceAsString();

        HotelDoc hotelDoc = JSON.parseObject(sourceAsString, HotelDoc.class);

        System.out.println(hotelDoc);

    }

    @Test
    //局部更新 文档
    void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "60214");

        request.doc(
               "city","北京",
                "starName","四星级"
        );

        client.update(request,RequestOptions.DEFAULT);

    }

    @Test
    //删除 文档
    void testDeleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel", "60214");

        client.delete(request,RequestOptions.DEFAULT);
    }

    @Test
    //批量导入文档
    void testBulkRequest() throws IOException {
        // 批量查询酒店数据
        List<Hotel> hotels = service.list();

        // 1.创建Request
        BulkRequest request = new BulkRequest();
        // 2.准备参数，添加多个新增的Request
        for (Hotel hotel : hotels) {
            // 2.1.转换为文档类型HotelDoc
            HotelDoc hotelDoc = new HotelDoc(hotel);
            // 2.2.创建新增文档的Request对象
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        // 3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }


    @BeforeEach
    void init() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://43.143.200.250:9200")
        ));
    }

    @AfterEach
    void Destruction() throws IOException {
        this.client.close();
    }
}

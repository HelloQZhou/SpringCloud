package com.qzhou.hotel;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


//导入静态属性
import static com.qzhou.hotel.constants.HotelConstants.MAPPING_TEMPLATE;


@SpringBootTest
class HotelDemoApplicationTests {

    private RestHighLevelClient client;

    @Test
    void createIndex() throws IOException {
        //创建索引库 ->
        //1.创建 CreateIndexRequest 对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");

        //2.准备请求的参数：DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);

        //3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    void deleteIndex() throws IOException {
        //删除索引库 ->
        //1.创建 CreateIndexRequest 对象
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        //2.发送请求
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void existIndex() throws IOException {
        //判断索引库存在 ->
        //1.创建 CreateIndexRequest 对象
        GetIndexRequest request = new GetIndexRequest("hotel");
        //2.发送请求
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists ?"索引库存在":"索引库不存在");
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

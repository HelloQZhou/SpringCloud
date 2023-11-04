package com.qzhou.hotel;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.QueryBindings;
import com.qzhou.hotel.pojo.HotelDoc;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static com.qzhou.hotel.constants.HotelConstants.MAPPING_TEMPLATE;


@SpringBootTest
class HotelSearchApplicationTests {

    private RestHighLevelClient client;

    @Test
    // DSL语句 match_all查询 转为Java语句实现
    void testSearchMatchAll() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        request.source().query(QueryBuilders.matchAllQuery());

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }


    @Test
     // DSL语句 match查询 转为Java语句实现
    void testSearchMatch() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        request.source()
                .query(QueryBuilders
                        .matchQuery("all","如家"));

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }

    @Test
    // DSL语句 精确查询 term   转为Java语句实现
    void testSearchTerm() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").lte(350).gte(200);


        request.source().query(rangeQueryBuilder);

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }
    @Test
    // DSL语句  范围查询 range  转为Java语句实现
    void testSearchRange() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("city", "上海");

        request.source().query(termQueryBuilder);

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }

    @Test
    // DSL语句  复合查询 bool  转为Java语句实现
    void testSearchBoll() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "如家");
        RangeQueryBuilder rangeQueryBuilder1 = QueryBuilders.rangeQuery("price").gt(400);
        RangeQueryBuilder rangeQueryBuilder2 = QueryBuilders.rangeQuery("price").gt(200);

        boolQueryBuilder.must(termQueryBuilder)
                        .mustNot(rangeQueryBuilder1)
                        .filter(rangeQueryBuilder2); //200< ~ <=400   {200,400]

        request.source().query(boolQueryBuilder);

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }



    //抽取的公共部分 -> 解析响应
    private void handleResponse(SearchResponse response) {
        SearchHits Searchhits = response.getHits();

        long value = Searchhits.getTotalHits().value;

        System.out.println("一共查询到的条数："+value);

        SearchHit[] hits = Searchhits.getHits();

        for (SearchHit hit : hits) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);

            //此时默认只会显示十条
            System.out.println("hotelDoc="+hotelDoc);
        }
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

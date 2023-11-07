package com.qzhou.hotel;

import com.alibaba.fastjson.JSON;
import com.qzhou.hotel.pojo.HotelDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
    void testSearchBool() throws IOException {

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

    @Test
        // DSL语句  分页 排序 查询   转为Java语句实现
    void testSearchPageAndSort() throws IOException {

        int page=1;
        int size=5;

        SearchRequest request=new SearchRequest("hotel");

        request.source().query(QueryBuilders.matchAllQuery());
        request.source().from((page-1)*size)
                        .size(size)
                        .sort("price", SortOrder.ASC);


        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }

    @Test
        // DSL语句 高亮显示   转为Java语句实现
    void testSearchHighLight() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        request.source().query(QueryBuilders.matchQuery("all","如家"));

        // 此时需要查询的字段 "name" 应为字符需传入 解析响应的函数 才好根据字段名进行高亮显示
        request.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }

    @Test
        // DSL语句 经纬度排序   转为Java语句实现
    void testSearchSort() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

         request.source().query(QueryBuilders.matchAllQuery())
                 .sort(SortBuilders
                         .geoDistanceSort("location",new GeoPoint("31.21,121.5"))
                         .order(SortOrder.ASC)
                         .unit(DistanceUnit.KILOMETERS)
                 );

        //执行DSL语句后 响应的数据
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        handleResponse(response);
    }

    @Test
        // DSL语句 聚合语法   转为Java语句实现
    void testSearchBuckets() throws IOException {

        SearchRequest request=new SearchRequest("hotel");

        request.source()
                .size(0)
                .aggregation(AggregationBuilders
                        .terms("brandAgg")
                        .field("brand")
                        .size(10)
                );

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //解析响应
        Aggregations aggregations = response.getAggregations();
        //注意 此时不能使用 .var 生成
        Terms terms=aggregations.get("brandAgg");

        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString());
        }
    }


    //抽取的公共部分 -> 解析响应
    private void handleResponse(SearchResponse response) {
        SearchHits Searchhits = response.getHits();

        long value = Searchhits.getTotalHits().value;

        System.out.println("一共查询到的条数："+value);

        SearchHit[] hits = Searchhits.getHits();

        for (SearchHit hit : hits) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            //判断此时如果有高亮显示的话要返回
            if(!CollectionUtils.isEmpty(highlightFields)){

                HighlightField highlightField = highlightFields.get("name");
                if(highlightField!=null) {
                    String name = highlightField.getFragments()[0].toString();
                    hotelDoc.setName(name);
                }
            }

            //判断是否有sort 距离
            if(!StringUtils.isEmpty(hit.getSourceAsString()))
            System.out.println(Arrays.toString(hit.getSortValues()));


//            //此时默认只会显示十条
//            System.out.println("hotelDoc="+hotelDoc);
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



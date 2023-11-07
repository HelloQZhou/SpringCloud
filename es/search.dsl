GET _search
{
  "query": {
    "match_all": {}
  }
}

Get /x

POST /_analyze
{
  "text": "你好哇，此时使用的是IK分词器，hello,只因你太美，奥里给",
  "analyzer": "ik_smart"
}


#创建索引库和映射
PUT /qzhou
{
  "mappings": {
    "properties": {
      "info":{
        "type": "text",
        "analyzer": "ik_smart"
      },
      "eamil":{
        "type": "keyword",
        "index": false
      },
      "name":{
        "properties": {
            "firstname":{
            "type": "text",
            "analyzer": "ik_smart"
          },
          "lastname":{
            "type": "text",
            "analyzer": "ik_smart"
          }
        }
      }

    }
  }
}


GET /qzhou



PUT /qzhou/_mapping
{
  "properties":{
    "age":{
      "type":"integer"
    }
  }
}


DELETE /qzhou



# 操作 文档

POST /qzhou/_doc/1
{
  "info":"概要描述",
  "email":"202@qq.com",
  "name":{
    "firstname":"Q",
    "lastname":"Zhou"
  }
}

GET /qzhou/_doc/1
GET /hotel/_doc/60214
GET /hotel/_search

DELETE /qzhou/_doc/1
DELETE /hotel/_doc/60214

# 全局更新
PUT /qzhou/_doc/1
{
  "email":"202@qq.com2",
  "name":{
    "firstname":"Q",
    "lastname":"Zhou"
  }
}

# 局部更新
POST /qzhou/_update/1
{
  "doc":{
  "info":"概要描述3"
  }
}



# 增删查 索引库

PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}


GET /hotel


DELETE /hotel




# 查询所有
GET /hotel/_search
{
  "query": {
    "match_all": {

    }
  }
}

# match查询：单字段查询
GET /hotel/_search
{
  "query": {
    "match": {
      "address": "广灵二路"
    }
  }
}

GET /hotel/_search
{
  "query": {
    "match": {
      "all":"如家"
    }
  }
}


# multi_match查询：多字段查询
GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "如家",
      "fields": ["brand","name","business"]
    }
  }
}

# term 精确查询
GET /hotel/_search
{
  "query": {
    "term": {
      "city": {
        "value": "上海"
      }
    }
  }
}




# range 范围查询 gte>=  lte<=
GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 200,
         "lte": 350
      }
    }
  }
}

# geo_distance 查询
GET /hotel/_search
{
  "query": {
    "geo_distance":{
      "distance":"1km",
      "location":"31.251433, 121.47522"
    }
  }
}

# 复合查询 function_score
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "match": {
          "city": "上海"
        }
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "7天酒店"
            }
          },
          "weight": 10
        }
      ],
      "boost_mode": "sum"

    }
  }
}


# 复合查询 bool
GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "如家"
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "price": {
              "gt": 400
            }
          }
        }
      ],
      "filter": [
        {
          "geo_distance": {
            "distance": "10km",
            "location": {
              "lat": 31.21,
              "lon": 121.5
            }
          }
        }
      ]
    }
  }
}

# 排序 查询
GET /hotel/_search
{
  "query": {
    "match_all": {

    }
  },
  "sort": [
    {
      "score" : "desc"
    },
    {
      "price": "asc"
    }
  ]
}


# 根据经纬度排序查询 lon 表示经度 lat纬度 经度范围大 纬度小
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
       "_geo_distance":
      {
        "location": {
          "lat": 22.507276,
          "lon": 113.931251
        },
        "order": "asc",
        "unit": "km"
      }
    }
  ]
}

# 分页查询
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "price":"asc"
    }
  ],
  "from": 990,
  "size": 10
}


# 高亮显示  默认添加标签 <em>

GET /hotel/_search
{
  "query": {
    "match": {
      "all": "如家"
    }
  },
  "highlight": {
    "fields": {
      "name": {
        "pre_tags": "<em>",
        "post_tags": "<em>",
        "require_field_match": "false"
      }
    }
  }
}

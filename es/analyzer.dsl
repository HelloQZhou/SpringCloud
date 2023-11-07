
# ik 分词器
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

# 拼音分词器

GET /_analyze
{
  "text": ["你好啊,还不错"],
  "analyzer": "pinyin"
}

# 自定义分词器
PUT /test
{
  "settings": {
    "analysis": {

      "analyzer": {
        "my_analyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },

      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }

    }
  },

  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}

DELETE /test

GET /test/_analyze
{
   "text": ["如家酒店还不错"],
  "analyzer": "my_analyzer"
}



# 自动补全功能

PUT test2
{
   "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}

POST test2/_doc
{
 "title": ["Sony", "WH-1000XM3"]
}

POST test2/_doc
{
 "title": ["SK-II","PITERA"]
}

POST test2/_doc
{
 "title": ["Nintendo", "switch"]
}

# 自动补全查询
GET /test2/_search
{
  "suggest": {
    "title_suggest": {
      "text": "S",
      "completion": {
        "field": "title",
        "skip_duplicates": true,
        "size": 10
      }
    }
  }
}


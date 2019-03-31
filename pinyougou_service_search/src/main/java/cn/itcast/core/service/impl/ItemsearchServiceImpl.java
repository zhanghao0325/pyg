package cn.itcast.core.service.impl;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.sellergoods.service.ItemsearchService;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Service
public class ItemsearchServiceImpl implements ItemsearchService {
    @Autowired
    SolrTemplate solrTemplate;
    //定义搜索对象的结构  category:商品分类
//    $scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));
        HashMap<String, Object> map = new HashMap<>();
        List<String> categoryList = findByCategory(searchMap);
        map.put("categoryList", categoryList);
        if (null != categoryList && categoryList.size() > 0) {
            map.putAll(searchBrandListAndSpecList(categoryList.get(0)));

        }
        map.putAll(search1(searchMap));
        return map;
    }

    public Map<String, Object> searchBrandListAndSpecList(String category) {
        HashMap<String, Object> map = new HashMap<>();
        Object typeId = redisTemplate.boundHashOps("itemCat").get(category);
        Object brandList = redisTemplate.boundHashOps("brandList").get(typeId);
        Object specList = redisTemplate.boundHashOps("specList").get(typeId);
        map.put("brandList", brandList);
        map.put("specList", specList);

        return map;
    }

    public Map<String, Object> search1(Map searchMap) {

        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        Object pageNo = searchMap.get("pageNo");
        Object pageSize = searchMap.get("pageSize");
        query.setOffset((Integer.parseInt(String.valueOf(pageNo)) - 1) * Integer.parseInt(String.valueOf(pageSize)));
        query.setRows(Integer.parseInt(String.valueOf(pageSize)));
        HighlightOptions item_title = new HighlightOptions().addField("item_title");
        item_title.setSimplePrefix("<em style='color:red'>");
        item_title.setSimplePostfix("</em>");
        query.setHighlightOptions(item_title);
        //根据商品分类查询
        if (!"".equals(searchMap.get("category"))){
            Criteria criteria1 = new Criteria("item_category").is(searchMap.get("category"));
            SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteria1);
            query.addFilterQuery(simpleFilterQuery);
        }
        //根据品牌分类查询
        if (!"".equals(searchMap.get("brand"))){
            Criteria criteria1 = new Criteria("item_brand").is(searchMap.get("brand"));
            SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteria1);
            query.addFilterQuery(simpleFilterQuery);
        }
        //根据规格查询
        if (null!=searchMap.get("spec")){
            Map<String,String> spec = (Map<String, String>) searchMap.get("spec");
            for (String s : spec.keySet()) {
                Criteria criteria1 = new Criteria("item_spec_" + s).is(searchMap.get(s));
                SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteria1);
                query.addFilterQuery(simpleFilterQuery);
            }
        }

        //根据价格查询
        if (!"".equals(searchMap.get("price"))){
            String[] strings = ((String) searchMap.get("prcie")).split("-");
            if (!strings[0].equals("0")){
                Criteria criteria1 = new Criteria("item_price").is(strings[0]);
                SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteria1);
                query.addFilterQuery(simpleFilterQuery);
            }
            if (!strings[1].equals("*")){
                Criteria criteria1 = new Criteria("item_price").is(strings[1]);
                SimpleFilterQuery simpleFilterQuery = new SimpleFilterQuery(criteria1);
                query.addFilterQuery(simpleFilterQuery);
            }
        }
        //排序
        String sort = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (null!=sort&&!"".equals(sort)){
            if (sort.equals("ASC")){
                Sort sort1 = new Sort(Sort.Direction.ASC,"item_"+sortField);
                query.addSort(sort1);

            }
            if (sort.equals("DESC")){
                Sort orders = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(orders);
            }
        }
        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        for (HighlightEntry<Item> entry : highlighted) {
            Item entity = entry.getEntity();
            if (null != entry.getHighlights() && entry.getHighlights().size() > 0) {
                String s = entry.getHighlights().get(0).getSnipplets().get(0);
                entity.setTitle(s);

            }
        }

        HashMap<String, Object> map = new HashMap<>();

        map.put("rows", items.getContent());
        map.put("total", items.getTotalElements());
        map.put("totalPages", items.getTotalPages());
        return map;
    }

    public List<String> findByCategory(Map searchMap) {
        ArrayList<String> list = new ArrayList<>();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        SimpleQuery query = new SimpleQuery(criteria);
        GroupOptions item_category = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(item_category);
        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
        GroupResult<Item> category = items.getGroupResult("item_category");
        for (GroupEntry<Item> entry : category.getGroupEntries().getContent()) {
            String groupValue = entry.getGroupValue();
            list.add(groupValue);
        }
        return list;
    }

}

package com.pinyougou.sellergoods.service.impl;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Autowired
    private TypeTemplateDao typeTemplateDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {
        List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
        for (TypeTemplate template : typeTemplates) {
            JSONArray brandlist = JSON.parseArray(template.getBrandIds());
            redisTemplate.boundHashOps("brandList").put(template.getId(),brandlist);
            List<Map> specList = findBySpecList(template.getId());

            redisTemplate.boundHashOps("specList").put(template.getId(),specList);
        }

//        System.out.println(typeTemplate);
        PageHelper.startPage(page, rows);
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        if (null != typeTemplate.getName() && !"".equals(typeTemplate.getName().trim())) {
            criteria.andNameLike("%" + typeTemplate.getName() + "%");
        }

        /*
        * 模板审核
        * */
        if (null != typeTemplate.getStatus() && !"".equals(typeTemplate.getStatus())){

            criteria.andStatusEqualTo(Long.valueOf(typeTemplate.getStatus()));
        }
        Page<TypeTemplate> pages = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        redisTemplate.boundHashOps("template").put("","");
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public void add( TypeTemplate typeTemplate) {
        if (null!=typeTemplate){
            typeTemplate.setStatus("0");
            typeTemplateDao.insertSelective(typeTemplate);
        }
    }
    @Override
    public void update(TypeTemplate typeTemplate) {

        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);

    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            typeTemplateDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> findTemplateList() {
        return typeTemplateDao.findTemplateList();

    }

    @Override
    public List<Map> findBySpecList(long id) {
        //查询
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        //得到specIds字段
        String specIds = typeTemplate.getSpecIds();
        //把specids字段变成json存有map集合的list集合
        List<Map> list = com.alibaba.fastjson.JSON.parseArray(specIds,Map.class);
        //遍历集合
        for (Map map : list) {
            //获取条件对象
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            //设置条件
            query.createCriteria().andSpecIdEqualTo((long)(Integer)map.get("id"));
            //在specificationOptionDao表中查询
            List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
            //把查询出来的list集合存入map集合中
            map.put("options",options);

        }
        //返回存有map集合的list集合
        return list;
    }
    /*
    * 模板 审核
    * */
    @Override
    public void updateStatus(long[] ids, String status) {

        TypeTemplate typeTemplate = new TypeTemplate();

        typeTemplate.setStatus(status);

        for (long id : ids) {

            typeTemplate.setId(id);

            typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
        }
    }


    @Override
    public TypeTemplate findOne(long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }
}

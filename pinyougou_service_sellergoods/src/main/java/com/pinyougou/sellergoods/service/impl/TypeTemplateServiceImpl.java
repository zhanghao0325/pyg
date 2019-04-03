package com.pinyougou.sellergoods.service.impl;

import cn.itcast.common.utils.ExcelUtil;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;

import com.alibaba.dubbo.config.annotation.Service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.pinyougou.sellergoods.service.TypeTemplateService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
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
            // JSONArray brandlist = JSON.parseArray(template.getBrandIds());

            List<Map> brandlist = JSON.parseArray(template.getBrandIds(), Map.class);
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
    public String ajaxUploadExcel(byte[] bytes) {
        System.out.println("得到数据文件");
        if (null == bytes) {
            try {
                throw new Exception("文件不存在！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InputStream in = null;
        try {
            in = new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加载流");
        List<List<Object>> list = null;
        try {
            System.out.println("加载流");
            list = new ExcelUtil().getBankListByExcel(in, "jjj");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        TypeTemplate vo = new TypeTemplate();
        for (int i = 0; i < list.size(); i++) {
            List<Object> lo = list.get(i);
            System.out.println("遍历" + list.get(i));
            TypeTemplate j = null;

            try {
                //j = studentmapper.selectByPrimaryKey(Long.valueOf());
                j = typeTemplateDao.selectByPrimaryKey(Long.valueOf(String.valueOf(lo.get(0))));
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                System.out.println("没有新增");
            }
            vo.setId(Long.valueOf(String.valueOf(lo.get(0))));
            vo.setName(String.valueOf(lo.get(1)));
            vo.setSpecIds(String.valueOf(lo.get(2)));
            vo.setBrandIds(String.valueOf(lo.get(3)));
            vo.setCustomAttributeItems(String.valueOf(lo.get(4)));
            vo.setStatus(String.valueOf(lo.get(5)));



            if (j == null) {
                typeTemplateDao.insertSelective(vo);
            } else {
                typeTemplateDao.updateByPrimaryKey(vo);
            }
        }
        return "very Good";
    }

    @Override
    public List<TypeTemplate> seleExecle() {
        return typeTemplateDao.selectByExample(null);
    }


    @Override
    public TypeTemplate findOne(long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }
}

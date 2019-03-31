package com.pinyougou.sellergoods.service.impl;


import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;


@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult search(Integer page, Integer rows, SearchEntity searchEntity) {
//        System.out.println(searchEntity);
        PageHelper.startPage(page, rows);
        SpecificationQuery specificationQuery = new SpecificationQuery();
        if (null != searchEntity && !"".equals(searchEntity.getName().trim())) {
            specificationQuery.createCriteria().andSpecNameLike("%" + searchEntity.getName() + "%");
        }
        Page<Specification> page1 = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        PageResult pageResult = new PageResult(page1.getTotal(), page1.getResult());
        return pageResult;

    }

    @Override
    public void add(SpecificationVo specificationVo) {
        specificationDao.insertSelective(specificationVo.getSpecification());
        for (SpecificationOption specificationOption : specificationVo.getSpecificationOptionList()) {
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            System.out.println(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);

        }
    }

    @Override
    public SpecificationVo findOne(long id) {
        Specification specification = specificationDao.selectByPrimaryKey(id);
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = specificationOptionQuery.createCriteria();
        criteria.andSpecIdEqualTo(id);

        List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
        SpecificationVo specificationVo = new SpecificationVo();
        specificationVo.setSpecification(specification);
        specificationVo.setSpecificationOptionList(specificationOptions);
        return specificationVo;
    }

    @Override
    public void update(SpecificationVo specificationVo) {
        specificationDao.updateByPrimaryKeySelective(specificationVo.getSpecification());
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(specificationVo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        for (SpecificationOption specificationOption : specificationVo.getSpecificationOptionList()) {
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }

    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            specificationDao.deleteByPrimaryKey(id);
        //删除规格属性表
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        specificationOptionDao.deleteByExample(query);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectAll();
    }


}

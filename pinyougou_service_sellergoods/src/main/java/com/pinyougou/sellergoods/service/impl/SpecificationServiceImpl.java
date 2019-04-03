package com.pinyougou.sellergoods.service.impl;


import cn.itcast.common.utils.ExcelUtil;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.good.Brand;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;
    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {

        PageHelper.startPage(page, rows);
        SpecificationQuery specificationQuery = new SpecificationQuery();
        if (null != specification.getSpecName() && !"".equals(specification.getSpecName().trim())) {
            specificationQuery.createCriteria().andSpecNameLike("%" + specification.getSpecName() + "%");
        }

        /*
        * 搜所审核状态
        * */
       if (null != specification.getSpecification_status() && !"".equals(specification.getSpecification_status())){
            specificationQuery.createCriteria().andSpecification_statusEqualTo(Long.valueOf(specification.getSpecification_status()));

        }
        Page<Specification> page1 = (Page<Specification>) specificationDao.selectByExample(specificationQuery);
        PageResult pageResult = new PageResult(page1.getTotal(), page1.getResult());
        return pageResult;

    }

    @Override
    public void add(SpecificationVo specificationVo) {
        specificationVo.getSpecification().setSpecification_status("0");
        specificationDao.insertSelective(specificationVo.getSpecification());
        for (SpecificationOption specificationOption : specificationVo.getSpecificationOptionList()) {
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            //System.out.println(specificationVo.getSpecification().getId());
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

    /*
    *
    *规格状态审核
    * */
    @Override
    public void updateStatus(long[] ids, String status) {

        Specification specification = new Specification();

        specification.setSpecification_status(status);

        for (long id : ids) {
            specification.setId(id);

            specificationDao.updateByPrimaryKeySelective(specification);

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
           Specification vo = new Specification();
            for (int i = 0; i < list.size(); i++) {
                List<Object> lo = list.get(i);
                System.out.println("遍历" + list.get(i));
                Specification j = null;

                try {
                    //j = studentmapper.selectByPrimaryKey(Long.valueOf());
                    j = specificationDao.selectByPrimaryKey(Long.valueOf(String.valueOf(lo.get(0))));
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    System.out.println("没有新增");
                }
                vo.setId(Long.valueOf(String.valueOf(lo.get(0))));
                vo.setSpecName(String.valueOf(lo.get(1)));
                vo.setSpecification_status(String.valueOf(lo.get(2)));


                if (j == null) {
                    specificationDao.insertSelective(vo);
                } else {
                    specificationDao.updateByPrimaryKey(vo);
                }
            }
            return "very Good";
        }

    @Override
    public List<Specification> seleExecle() {
        return specificationDao.selectByExample(null);
    }
}




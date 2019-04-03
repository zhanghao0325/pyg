package com.pinyougou.sellergoods.service.impl;

import cn.itcast.common.utils.ExcelUtil;
import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, Brand brand) {


            PageHelper.startPage(page,rows);
            BrandQuery brandQuery = new BrandQuery();
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (null!=brand){
            if (null != brand.getName() && !"".equals(brand.getName().trim())) {

                criteria.andNameLike("%" + brand.getName().trim() + "%");
            }
            if (null != brand.getFirstChar() && !"".equals(brand.getFirstChar().trim())) {

                criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
            }
            if (null != brand.getBrand_status() &&!"".equals(brand.getBrand_status())){

                criteria.andBrand_StatusEqualTo(Long.valueOf(brand.getBrand_status()));
            }

        }


            Page<Brand> list = (Page<Brand>) brandDao.selectByExample(brandQuery);



            PageResult pageResult = new PageResult(list.getTotal(), list.getResult());


            return pageResult;


    }

    @Override
    public void save(Brand brand) {


        brand.setBrand_status("0");

        brandDao.insertSelective(brand);
    }

    @Override
    public Brand findOne(long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(long[] ids) {
        for (long id : ids) {
            brandDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return  brandDao.selectAll();

    }
     /*
     * 审核状态
     * */
    @Override
    public void updateStatus(long[] ids, String status) {
        Brand brand = new Brand();
        brand.setBrand_status(status);

        for (long id : ids) {
            brand.setId(id);
            brandDao.updateByPrimaryKeySelective(brand);
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
        Brand vo = new Brand();
        for (int i = 0; i < list.size(); i++) {
            List<Object> lo = list.get(i);
            System.out.println("遍历" + list.get(i));
            Brand j = null;

            try {
                //j = studentmapper.selectByPrimaryKey(Long.valueOf());
                j = brandDao.selectByPrimaryKey(Long.valueOf(String.valueOf(lo.get(0))));
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                System.out.println("没有新增");
            }
            vo.setId(Long.valueOf(String.valueOf(lo.get(0))));
            vo.setName(String.valueOf(lo.get(1)));
            vo.setFirstChar(String.valueOf(lo.get(2)));
            vo.setBrand_status(String.valueOf(lo.get(3)));

            if (j == null) {
                brandDao.insertSelective(vo);
            } else {
                brandDao.updateByPrimaryKey(vo);
            }
        }
        return "very Good";
    }

    @Override
    public List<Brand> seleExecle() {
        return brandDao.selectByExample(null);
    }

}

package cn.itcast.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import entity.SearchEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {
    @Reference
    SpecificationService specificationService;

    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody(required = false) SearchEntity searchEntity) {
        PageResult pageResult = specificationService.search(page, rows, searchEntity);

        return pageResult;

    }

    @RequestMapping("add")
    public Result add(@RequestBody SpecificationVo specificationVo) {
        try {
            specificationService.add(specificationVo);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "失败");
        }

    }

    @RequestMapping("findOne")
    public SpecificationVo findOne(long id) {
        return specificationService.findOne(id);

    }

    @RequestMapping("update")
    public Result update(@RequestBody SpecificationVo specificationVo) {
        try {
            specificationService.update(specificationVo);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "失败");
        }

    }

    @RequestMapping("delete")
    public Result delete(long[] ids) {
        try {
            specificationService.delete(ids);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "失败");
        }
    }
    @RequestMapping("selectOptionList")
    public List<Map> selectOptionList(){
        return specificationService.selectOptionList();
    }
}

package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.Goods;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import entity.SearchEntity;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("all")
@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("findAll")
    public List<Brand> findAll() {
        List<Brand> list = brandService.findAll();
        return list;
    }

    @RequestMapping("search")
    public PageResult findPage(Integer page, Integer rows, @RequestBody(required = false) Brand brand) {
        System.out.println("1");

        return brandService.findPage(page, rows, brand);


    }

    @RequestMapping("save")
    public Result save(@RequestBody Brand brand) {
        try {
            brandService.save(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            return new Result(false, "保存失败");

        }

    }

    @RequestMapping("update")
    public Result update(@RequestBody Brand brand) {
        try {
            brandService.update(brand);
            return new Result(true, "success");
        } catch (Exception e) {
            return new Result(false, "保存失败");

        }

    }

    @RequestMapping("findOne")
    public Brand findOne(long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(@RequestBody long[] deletelist) {

//        System.out.println(deletelist);
        try {
            brandService.delete(deletelist);
            return new Result(true, "success");
        } catch (Exception e) {
            return new Result(false, "删除失败");

        }

    }

    //查询所有品牌
    @RequestMapping("selectOptionList")
    public List<Map> selectOptionList() {

        return brandService.selectOptionList();
}

    //修改审核状态
    @RequestMapping("updateStatus")
    public  Result  updateStatus(long[] ids,String status){

        try {
            brandService.updateStatus(ids,status);
            return new Result(true,"审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"驳回");
        }

    }


    /**
     * 品牌Excel表导入数据库
     */
    @RequestMapping("/ajaxUpload")
    public String ajaxUploadExcel(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        return brandService.ajaxUploadExcel(bytes);
    }
    @RequestMapping("/derive")
    public Result Execle(HttpServletRequest request ){

        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("ss");
        HSSFCellStyle Style1 = wb.createCellStyle();
        Style1.setAlignment(HorizontalAlignment.CENTER);
        //在sheet里创建第一行，参数为行索引
        List<Brand> spc1 = brandService.seleExecle();

        System.out.println(spc1);
        System.out.println(spc1);
        HSSFRow row2 = sheet.createRow(0);
        Style1.setAlignment(HorizontalAlignment.CENTER);
        row2.createCell(0).setCellValue("商品表");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));

        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("id");
        //row1.createCell(1).setCellValue("parent_id");
        row1.createCell(2).setCellValue("name");
        //row1.createCell(3).setCellValue("type_id");
        row1.createCell(4).setCellValue("frist_char");
        row1.createCell(6).setCellValue("brand_status");
        int i=2;
        for (Brand spc : spc1) {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(spc.getId());
            // row.createCell(1).setCellValue(spc.getParentId());
            row.createCell(2).setCellValue(spc.getName());
            //row.createCell(3).setCellValue(spc.getTypeId());
            row.createCell(4).setCellValue(spc.getFirstChar());
            row.createCell(6).setCellValue(spc.getBrand_status());
            i++;
        }
//        HSSFRow row1=sheet.createRow(0);
//        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个 ）
//        HSSFCell cell=row1.createCell(0);
//        //设置单元格内容
//        cell.setCellValue("一览表");
//        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));

        try {
//            OutputStream output=response.getOutputStream();
//            response.reset();
//            response.setHeader("Content-disposition", "attachment; filename=details.xls");
//            response.setContentType("application/msexcel");
            String realPath = request.getSession().getServletContext().getRealPath("/");
            System.out.println(realPath);
            UUID uuid = UUID.randomUUID();
            FileOutputStream FOut = new FileOutputStream(realPath+uuid+".xls");
            wb.write(FOut);
            FOut.close();
            return   new Result(true,"导出成功了");
        } catch (Exception e) {
            e.printStackTrace();
            return    new Result(false,"导出失败了");
        }

//        FileOutputStream FOut = null;
//        return wb;

    }


}

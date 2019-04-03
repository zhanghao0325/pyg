package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.ItemCatService;
import entity.PageResult;
import entity.Result;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("all")
@RestController
@RequestMapping("itemCat")
public class ItemCatController {
    @Reference
    ItemCatService itemCatService;

    @RequestMapping("findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }



    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody ItemCat itemCat) {
        return itemCatService.search(page, rows, itemCat);

    }

    @RequestMapping("findByParentId")
    public List<ItemCat> findByParentId(long parentId) {

        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("add")
    public Result save(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.save(itemCat);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }
    }

    @RequestMapping("findOne")
    public ItemCat findOne(long id) {
        return itemCatService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
            return new Result(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "false");
        }

    }

    @RequestMapping("delete")
    public Result delete(long[] ids) {
        try {
            itemCatService.delete(ids);
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"false");
        }
    }
    @RequestMapping("/Execle")
    public Result Execle(HttpServletRequest request){
//        String outputFile="D:\\brand.xls";
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("ss");
        HSSFCellStyle Style1 = wb.createCellStyle();
        Style1.setAlignment(HorizontalAlignment.CENTER);
        //在sheet里创建第一行，参数为行索引
        List<ItemCat> spc1 = itemCatService.seleExecle();
        System.out.println(spc1);
        HSSFRow row2 = sheet.createRow(0);
        Style1.setAlignment(HorizontalAlignment.CENTER);
        row2.createCell(0).setCellValue("分类数据表");
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));

        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("id");
        //row1.createCell(1).setCellValue("parent_id");
        row1.createCell(2).setCellValue("name");
       //row1.createCell(3).setCellValue("type_id");
        row1.createCell(4).setCellValue("status");
        int i=2;
        for (ItemCat spc : spc1) {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0).setCellValue(spc.getId());
           // row.createCell(1).setCellValue(spc.getParentId());
            row.createCell(2).setCellValue(spc.getName());
            //row.createCell(3).setCellValue(spc.getTypeId());
            row.createCell(4).setCellValue(spc.getStatus());
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
    @RequestMapping("/ajaxUpload")
    public String ajaxUploadExcel(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        return itemCatService.ajaxUploadExcel(bytes);
    }

}
